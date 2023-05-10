package de.innovationhub.prox.modules.user.contract.search.dto;

import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record SearchPreferencesDto(
    UUID userId,
    List<TagDto> tags,
    ProjectSearchDto projectSearch,
    OrganizationSearchDto organizationSearch,
    LecturerSearchDto lecturerSearch
) {
  public record ProjectSearchDto(
      Boolean enabled,
      Set<ModuleTypeDto> moduleTypes,
      Set<DisciplineDto> disciplines
  ) {}

  public record OrganizationSearchDto(
      Boolean enabled
  ) {}

  public record LecturerSearchDto(
      Boolean enabled
  ) {}
}
