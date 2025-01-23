package com.hanaro.endingcredits.endingcreditsapi.utils.mapper;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", expression = "java(entity.getProductId() != null ? entity.getProductId().toString() : null)")
    @Mapping(target = "productName", source = "entity.productName")
    @Mapping(target = "productArea", source = "entity.productArea.description")
    @Mapping(target = "company", source = "entity.company")
    PensionSavingsSearchItems toPensionSavingsSearchItems(PensionSavingsProductEntity entity);

    @Mapping(target = "companyId", expression = "java(entity.getCompanyId() != null ? entity.getCompanyId().toString() : null)")
    @Mapping(target = "company", source = "entity.company")
    @Mapping(target = "area", source = "entity.area.description")
    RetirementPensionSearchItems toRetirementPensionSearchItems(RetirementPensionCompanyEntity entity);

    @Named("setDefaultContractRate")
    static BigDecimal setDefaultContractRate(BigDecimal contractRate) {
        return contractRate != null ? contractRate : BigDecimal.ZERO;
    }
}
