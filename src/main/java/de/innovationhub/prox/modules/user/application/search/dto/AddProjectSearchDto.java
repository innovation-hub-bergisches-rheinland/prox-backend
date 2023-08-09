package de.innovationhub.prox.modules.user.application.search.dto;

import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.Collection;
import java.util.UUID;

public record AddProjectSearchDto(
    String text,
    Collection<ProjectState> states,
    Collection<String> disciplineKeys,
    Collection<String> moduleTypeKeys,
    Collection<UUID> tagIds
) {

}
