package de.innovationhub.prox.modules.user.application.search.dto;

import de.innovationhub.prox.modules.user.domain.search.ProjectSearch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record CreateSearchPreferencesRequest(
    UUID tagCollectionId,
    @NotNull ProjectSearchRequest projectSearch,
    @NotNull OrganizationSearchRequest organizationSearch,
    @NotNull LecturerSearchRequest lecturerSearch
) {
  public record ProjectSearchRequest(
      boolean enabled,
      Set<@NotBlank String> moduleTypes,
      Set<@NotBlank String> disciplines
  ) {}

  public record OrganizationSearchRequest(
      boolean enabled
  ) {}

  public record LecturerSearchRequest(
      boolean enabled
  ) {}
}
