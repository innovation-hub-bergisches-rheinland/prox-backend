package de.innovationhub.prox.modules.project.contract;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record ProjectView(
    UUID id,
    UUID partner,
    List<UUID> supervisors,
    Collection<UUID> tags
) {

}
