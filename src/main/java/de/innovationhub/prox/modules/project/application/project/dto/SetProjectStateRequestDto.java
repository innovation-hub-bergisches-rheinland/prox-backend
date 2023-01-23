package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SetProjectState")
public record SetProjectStateRequestDto(
    ProjectState state
) {

}
