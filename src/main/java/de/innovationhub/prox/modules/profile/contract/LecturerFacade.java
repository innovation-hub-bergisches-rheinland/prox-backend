package de.innovationhub.prox.modules.profile.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LecturerFacade {
    Optional<LecturerView> get(UUID id);
    List<LecturerView> findByIds(List<UUID> ids);
}
