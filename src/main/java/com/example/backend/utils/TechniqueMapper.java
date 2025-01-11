package com.example.backend.utils;

import com.example.backend.dto.TechniqueDto;
import com.example.backend.models.Technique;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TechniqueMapper {
    Technique toEntity(TechniqueDto techniqueDto);

    TechniqueDto toDto(Technique technique);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Technique partialUpdate(TechniqueDto techniqueDto, @MappingTarget Technique technique);
}