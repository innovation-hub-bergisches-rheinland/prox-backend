package de.innovationhub.prox.modules.project.application.module.dto;

import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface ModuleTypeMapper {
  ModuleTypeDto toDto(ModuleType moduleType);
  List<ModuleTypeDto> toDtoList(List<ModuleType> moduleTypes);
}
