package de.innovationhub.prox.modules.project.application.project.web.dto;

import java.util.List;

public record ReadProjectListDto(
    List<ProjectDto> projects
) {

}
