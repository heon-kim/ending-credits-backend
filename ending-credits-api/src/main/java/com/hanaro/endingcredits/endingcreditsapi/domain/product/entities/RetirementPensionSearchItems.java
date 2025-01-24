package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "retirement_pension_search_items")
public class RetirementPensionSearchItems {

    @Id
    private String companyId;

    @Field(type = FieldType.Text)
    private String company;

    @Field(type = FieldType.Keyword)
    private String area;
}
