import os
import sys

OUTPUT_FILE = "project_context.txt"

EXCLUDED_DIRS = {
    ".git", ".idea", ".vscode", "target", "build",
    ".gradle", "bin", "out", "node_modules", "__pycache__"
}

BINARY_EXTENSIONS = {
    ".class", ".jar", ".war", ".ear", ".zip", ".tar", ".gz",
    ".png", ".jpg", ".jpeg", ".gif", ".ico", ".svg",
    ".exe", ".dll", ".so", ".dylib", ".pdf"
}

EXPLICIT_TEXT_FILES = {
    "Dockerfile", "dockerfile", "Makefile", "makefile",
    ".env.example", ".gitignore", ".gitattributes", ".editorconfig"
}

ALLOWED_EXTENSIONS = {
    ".java", ".xml", ".properties", ".yml", ".yaml",
    ".sql", ".gradle", ".md", ".json", ".sh", ".bash",
    ".toml", ".graphql", ".proto", ".txt"
}

EXCLUDED_FILES = {
    OUTPUT_FILE, "mvnw", "mvnw.cmd", "gradlew", "gradlew.bat"
}

def is_source_file(filename):
    if filename in EXPLICIT_TEXT_FILES:
        return True
    _, ext = os.path.splitext(filename)
    if ext.lower() in BINARY_EXTENSIONS:
        return False
    return ext.lower() in ALLOWED_EXTENSIONS

def generate_tree(startpath):
    tree_lines = []
    for root, dirs, files in os.walk(startpath):
        dirs[:] = [d for d in dirs if d not in EXCLUDED_DIRS]
        level = root.replace(startpath, '').count(os.sep)
        indent = ' ' * 4 * level
        tree_lines.append(f"{indent}{os.path.basename(root)}/")
        subindent = ' ' * 4 * (level + 1)
        for f in sorted(files):
            if f not in EXCLUDED_FILES and is_source_file(f):
                tree_lines.append(f"{subindent}{f}")
    return "\n".join(tree_lines)

def main():
    root_dir = os.getcwd()

    print(f"Đang quét thư mục: {root_dir}")

    with open(OUTPUT_FILE, "w", encoding="utf-8") as outfile:
        outfile.write("=" * 80 + "\n")
        outfile.write("PROJECT DIRECTORY STRUCTURE\n")
        outfile.write("=" * 80 + "\n\n")
        outfile.write(generate_tree(root_dir))
        outfile.write("\n\n")

        count = 0
        for root, dirs, files in os.walk(root_dir):
            dirs[:] = [d for d in dirs if d not in EXCLUDED_DIRS]

            for file in sorted(files):
                if file in EXCLUDED_FILES or not is_source_file(file):
                    continue

                file_path = os.path.join(root, file)
                rel_path = os.path.relpath(file_path, root_dir)

                outfile.write("=" * 80 + "\n")
                outfile.write(f"FILE: {rel_path}\n")
                outfile.write("=" * 80 + "\n\n")

                try:
                    with open(file_path, "r", encoding="utf-8", errors="replace") as infile:
                        outfile.write(infile.read())
                    outfile.write("\n\n")
                    count += 1
                except Exception as e:
                    outfile.write(f"[Lỗi đọc file: {e}]\n\n")

    print(f"Đã xuất thành công {count} files vào: {OUTPUT_FILE}")

if __name__ == "__main__":
    main()