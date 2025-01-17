package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class ExchangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="exchange_id")
    private Long exchangeId;

    @Column(nullable = false, name = "exchange_name")
    private String exchangeName;
}
