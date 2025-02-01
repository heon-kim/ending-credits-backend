package com.hanaro.endingcredits.endingcreditsapi.domain.will.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "will_file")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WillFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID willFileId;

    @ManyToOne
    @JoinColumn(name = "will_id", nullable = false)
    private WillEntity will;

    @Column(nullable = false)
    private String fileUrl;
}
