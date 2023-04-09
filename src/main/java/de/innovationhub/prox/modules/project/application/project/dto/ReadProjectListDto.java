package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.project.contract.dto.ProjectDto;
import java.util.List;

public record ReadProjectListDto(
    List<ProjectDto> projects
) {

}
