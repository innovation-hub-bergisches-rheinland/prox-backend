package de.innovationhub.prox.modules.user.application.search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record CreateSearchPreferencesRequest(
    // Let's not set the tags manually, but instead use the tag collection of the user profile
    // List<UUID> tags,
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
