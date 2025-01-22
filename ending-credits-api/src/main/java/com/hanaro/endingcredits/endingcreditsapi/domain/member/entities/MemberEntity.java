package com.hanaro.endingcredits.endingcreditsapi.domain.member.entities;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.LoginType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "member")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private String identifier;

    @Column(nullable = false)
    private String password;

    @Column
    private String simplePassword;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장 (NORMAL, KAKAO)
    private LoginType loginType;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean isLinked = false;

    @Column(nullable = false)
    @Builder.Default
    private Long wishFund = 0L;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetEntity> assets = new ArrayList<>();
}
