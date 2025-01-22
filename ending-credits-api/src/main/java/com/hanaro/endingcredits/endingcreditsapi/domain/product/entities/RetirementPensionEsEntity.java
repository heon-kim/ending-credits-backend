package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "retirement_pension")
public class RetirementPensionEsEntity {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String productName;

    @Field(type = FieldType.Keyword)
    private ProductArea productArea;

    @Field(type = FieldType.Keyword)
    private SysType sysType;

    @Field(type = FieldType.Text)
    private String company;
}
