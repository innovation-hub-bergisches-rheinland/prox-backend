package de.innovationhub.prox.modules.project.application.project.web.dto;

import de.innovationhub.prox.modules.project.domain.project.ProjectState;

public record SetProjectStateRequestDto(
    ProjectState state
) {

}
