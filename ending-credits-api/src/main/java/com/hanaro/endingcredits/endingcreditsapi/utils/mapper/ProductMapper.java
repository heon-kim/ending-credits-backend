package com.hanaro.endingcredits.endingcreditsapi.utils.mapper;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", expression = "java(entity.getProductId() != null ? entity.getProductId().toString() : null)")
    @Mapping(target = "productName", source = "entity.productName")
    @Mapping(target = "productArea", source = "entity.productArea.description")
    @Mapping(target = "company", source = "entity.company")
    PensionSavingsEsEntity toPensionSavingsEsEntity(PensionSavingsProductEntity entity);

//    @Mapping(target = "id", expression = "java(entity.getProductId() != null ? entity.getProductId().toString() : null)")
//    @Mapping(target = "productName", source = "entity.productName")
//    @Mapping(target = "company", source = "entity.company")
//    @Mapping(target = "applyTerm", source = "entity.applyTerm")
//    @Mapping(target = "checkDate", source = "entity.checkDate")
//    @Mapping(target = "contractTerm", source = "entity.contractTerm")
//    @Mapping(target = "contractRate", source = "entity.contractRate")
//    @Mapping(target = "productArea", expression = "java(entity.getProductArea() != null ? entity.getProductArea() : null)")
//    @Mapping(target = "sysType", source = "entity.sysType")
//    RetirementPensionEsEntity toRetirementPensionEsEntity(RetirementPensionYieldEntity entity);
//
//    @Mapping(target = "productName", source = "dto.product")
//    @Mapping(target = "company", source = "dto.company")
//    @Mapping(target = "applyTerm", source = "dto.applyTerm")
//    @Mapping(target = "checkDate", source = "dto.checkDate")
//    @Mapping(target = "contractTerm", source = "dto.contractTerm")
//    @Mapping(target = "contractRate", source = "dto.contractRate", qualifiedByName = "setDefaultContractRate")
//    @Mapping(target = "productArea", source = "productArea")
//    @Mapping(target = "sysType", source = "sysType")
//    RetirementPensionYieldEntity toRetirementPensionYieldEntity(RetirementPensionProductDto dto, ProductArea productArea, SysType sysType);

    @Named("setDefaultContractRate")
    static BigDecimal setDefaultContractRate(BigDecimal contractRate) {
        return contractRate != null ? contractRate : BigDecimal.ZERO;
    }
}
