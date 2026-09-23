import os

OUTPUT_FILE = "project_context.txt"

EXCLUDED_DIRS = {
    ".git", ".idea", ".vscode", "target", "build",
    ".gradle", "bin", "out", "node_modules"
}

ALLOWED_EXTENSIONS = {
    ".java", ".xml", ".properties", ".yml", ".yaml",
    ".sql", ".gradle", ".md", ".json"
}

EXCLUDED_FILES = {
    OUTPUT_FILE, "mvnw", "mvnw.cmd", "gradlew", "gradlew.bat"
}

def is_text_file(filename):
    return any(filename.endswith(ext) for ext in ALLOWED_EXTENSIONS)

def main():
    root_dir = os.path.dirname(os.path.abspath(__file__))

    with open(OUTPUT_FILE, "w", encoding="utf-8") as outfile:
        for root, dirs, files in os.walk(root_dir):
            dirs[:] = [d for d in dirs if d not in EXCLUDED_DIRS]

            for file in sorted(files):
                if file in EXCLUDED_FILES or not is_text_file(file):
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
                except Exception as e:
                    outfile.write(f"[Lỗi đọc file: {e}]\n\n")

    print(f"exported: {OUTPUT_FILE}")

if __name__ == "__main__":
    main()