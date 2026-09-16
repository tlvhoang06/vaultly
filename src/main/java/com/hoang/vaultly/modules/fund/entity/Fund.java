package com.hoang.vaultly.modules.fund.entity;

import com.hoang.vaultly.modules.fund.enums.FundStatus;
import com.hoang.vaultly.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "funds")
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, length = 100)
    String name;

    @Column(length = 1000)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    FundStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
            @JoinColumn(name = "created_by", nullable = false, updatable = false)
    User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @OneToMany(mappedBy = "fund", fetch =FetchType.LAZY)
    Set<FundMember> members = new HashSet<>();

}
