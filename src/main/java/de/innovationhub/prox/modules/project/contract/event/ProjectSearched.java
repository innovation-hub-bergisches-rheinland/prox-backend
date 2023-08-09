package de.innovationhub.prox.modules.project.contract.event;

import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.Collection;
import java.util.UUID;

public record ProjectSearched(
    String text,
    Collection<ProjectState> states,
    Collection<String> specializationKeys,
    Collection<String> moduleTypeKeys,
    Collection<UUID> tagIds
) {

}
