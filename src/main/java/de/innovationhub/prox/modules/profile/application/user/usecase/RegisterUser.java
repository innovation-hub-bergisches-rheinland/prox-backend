package de.innovationhub.prox.modules.profile.application.user.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.UUID;

public record RegisterUser(UUID id, String name, String email) implements UseCase {

}
