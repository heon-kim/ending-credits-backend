package com.hanaro.endingcredits.endingcreditsapi.utils.mapper;

import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.WillInfoDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.WillRequestDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.entities.WillEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.entities.WillFileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WillMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "member", ignore = true)
    WillEntity toWillEntity(WillRequestDto willRequestDto);

    @Mapping(source = "willFiles", target = "files", qualifiedByName = "mapFiles")
    WillInfoDto toWillInfoDto(WillEntity willEntity);

    @Named("mapFiles")
    default List<String> mapFiles(List<WillFileEntity> willFiles) {
        return willFiles.stream()
                .map(WillFileEntity::getFileUrl)
                .collect(Collectors.toList());
    }
}
