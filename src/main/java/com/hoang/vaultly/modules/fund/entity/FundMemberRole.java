package com.hoang.vaultly.modules.fund.entity;

import com.hoang.vaultly.modules.fund.enums.FundRole;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "fund_member_roles")
public class FundMemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @JoinColumn(name = "fund_member_id")
    @ManyToOne
    FundMember fundMember;

    @Enumerated(EnumType.STRING)
    FundRole role;

    @CreationTimestamp
    Instant grantedAt;
}
