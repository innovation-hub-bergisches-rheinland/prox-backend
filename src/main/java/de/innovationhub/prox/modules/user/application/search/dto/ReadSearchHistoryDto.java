package de.innovationhub.prox.modules.user.application.search.dto;

import de.innovationhub.prox.modules.project.contract.dto.DisciplineDto;
import de.innovationhub.prox.modules.project.contract.dto.ModuleTypeDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record ReadSearchHistoryDto(
    UUID userId,
    List<ProjectSearchEntryDto> projectSearches
) {

  public record ProjectSearchEntryDto(
      String text,
      Collection<TagDto> tags,
      Collection<ProjectState> states,
      Collection<DisciplineDto> disciplines,
      Collection<ModuleTypeDto> moduleTypes,
      int newProjects
  ) {

  }
}
