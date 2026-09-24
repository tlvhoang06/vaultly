package com.hoang.vaultly.modules.user.entity;

import com.hoang.vaultly.modules.user.enums.SystemRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "Username contains 3-20 characters, only " +
            "alphabet/number/underline accepted")
    @Column(unique = true, nullable = false)
    String username;

    @Column(unique = true)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String displayName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    SystemRole systemRole = SystemRole.USER;
}
