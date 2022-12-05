package de.innovationhub.prox.modules.project.application.module.web.dto;

import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper
public interface ModuleTypeMapper {
  ReadModuleTypeDto toDto(ModuleType moduleType);
  default Page<ReadModuleTypeDto> toDtoPage(Page<ModuleType> moduleTypes) {
    return moduleTypes.map(this::toDto);
  }
}
