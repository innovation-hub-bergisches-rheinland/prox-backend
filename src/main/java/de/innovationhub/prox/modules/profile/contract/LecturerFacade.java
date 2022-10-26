package de.innovationhub.prox.modules.profile.contract;

import java.util.UUID;

public interface LecturerFacade {
    LecturerView get(UUID id);
}
