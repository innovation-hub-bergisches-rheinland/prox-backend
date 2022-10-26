package de.innovationhub.prox.modules.profile.contract;

import java.util.Optional;
import java.util.UUID;

public interface LecturerFacade {
    Optional<LecturerView> get(UUID id);
}
