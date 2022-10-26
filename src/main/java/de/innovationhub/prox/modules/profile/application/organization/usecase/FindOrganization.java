package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.UUID;

public record FindOrganization(UUID id) implements UseCase {

}
