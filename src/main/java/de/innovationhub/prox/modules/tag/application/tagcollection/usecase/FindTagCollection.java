package de.innovationhub.prox.modules.tag.application.tagcollection.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.UUID;

public record FindTagCollection(UUID id) implements UseCase {

}
