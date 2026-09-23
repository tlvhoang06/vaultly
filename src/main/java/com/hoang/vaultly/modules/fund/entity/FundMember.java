package com.hoang.vaultly.modules.fund.entity;

import com.hoang.vaultly.modules.fund.enums.FundRole;
import com.hoang.vaultly.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
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

    @Builder.Default
    @OneToMany(mappedBy = "fundMember", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<FundMemberRole> roles = new HashSet<>();

    public void addRole(FundRole role){
        if(this.roles == null){
            this.roles = new HashSet<>();
        }
        FundMemberRole memberRole = FundMemberRole.builder()
                .role(role)
                .fundMember(this)   // back assign: FundMemberRole -> FundMember
                .build();

        this.roles.add(memberRole); // forth assign: FundMember -> FundMemberRole
    }

}
