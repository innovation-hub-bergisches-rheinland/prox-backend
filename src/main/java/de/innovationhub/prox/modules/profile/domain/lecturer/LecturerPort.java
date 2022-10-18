package de.innovationhub.prox.modules.profile.domain.lecturer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LecturerPort {

  Lecturer save(Lecturer lecturer);

  List<Lecturer> getAll();

  Optional<Lecturer> getById(UUID id);

  boolean existsByUserId(UUID id);
}
