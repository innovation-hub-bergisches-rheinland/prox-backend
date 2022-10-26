package de.innovationhub.prox.modules.project.application.module.dto;

import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ModuleTypeMapper {
  ReadModuleTypeDto toDto(ModuleType moduleType);
  List<ReadModuleTypeDto> toDtoList(List<ModuleType> moduleTypes);
}
