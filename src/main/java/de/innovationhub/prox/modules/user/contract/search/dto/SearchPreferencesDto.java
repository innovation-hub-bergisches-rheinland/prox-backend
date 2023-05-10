package de.innovationhub.prox.modules.user.contract.search.dto;

import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
import java.util.Set;
import java.util.UUID;

public record SearchPreferencesDto(
    UUID userId,
    UUID tagCollectionId,
    ProjectSearchDto projectSearch,
    OrganizationSearchDto organizationSearch,
    LecturerSearchDto lecturerSearch
) {
  public record ProjectSearchDto(
      Boolean enabled,
      Set<String> moduleTypes,
      Set<String> disciplines
  ) {}

  public record OrganizationSearchDto(
      Boolean enabled
  ) {}

  public record LecturerSearchDto(
      Boolean enabled
  ) {}
}
