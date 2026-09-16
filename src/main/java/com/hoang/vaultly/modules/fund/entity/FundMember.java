package com.hoang.vaultly.modules.fund.entity;

import com.hoang.vaultly.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "fund_members")
public class FundMember {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    UUID id;

    @ManyToOne
    User user;

    @ManyToOne
    Fund fund;

    BigDecimal contributionRatio;

    @CreationTimestamp
    Instant joinedAt;

    @OneToMany(mappedBy = "fundMember", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<FundMemberRole> roles = new HashSet<>();


}
