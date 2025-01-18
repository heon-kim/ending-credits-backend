package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class ExchangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="exchange_id")
    private UUID exchangeId;

    @Column(nullable = false, name = "exchange_name")
    private String exchangeName;
}
