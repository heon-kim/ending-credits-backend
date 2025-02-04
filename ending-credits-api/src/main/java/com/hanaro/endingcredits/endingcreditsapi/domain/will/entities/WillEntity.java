package com.hanaro.endingcredits.endingcreditsapi.domain.will.entities;

import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "will")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID willId;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(nullable = false)
    private String willCodeId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private WillCreatedType createdType;

    @Column(nullable = false)
    private Integer shareAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "will")
    private List<WillFileEntity> willFiles = new ArrayList<>();
}
