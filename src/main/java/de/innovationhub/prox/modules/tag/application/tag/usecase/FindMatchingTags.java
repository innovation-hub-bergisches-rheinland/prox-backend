package de.innovationhub.prox.modules.tag.application.tag.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;

public record FindMatchingTags(String partialTag) implements UseCase {

}
