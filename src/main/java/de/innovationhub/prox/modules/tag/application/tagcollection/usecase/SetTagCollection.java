package de.innovationhub.prox.modules.tag.application.tagcollection.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.List;
import java.util.UUID;

public record SetTagCollection(
    UUID id,
    List<String> tags
) implements UseCase {

}
