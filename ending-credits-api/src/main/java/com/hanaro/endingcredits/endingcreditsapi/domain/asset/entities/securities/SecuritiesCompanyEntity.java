package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SecuritiesCompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "securities_company_id")
    private UUID securitiesCompanyId; //증권사ID

    @Column(nullable = false,name = "securities_company_name")
    private String securitiesCompanyName; //증권사명
}
