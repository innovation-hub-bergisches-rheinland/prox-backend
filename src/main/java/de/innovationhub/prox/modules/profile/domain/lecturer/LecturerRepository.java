package de.innovationhub.prox.modules.profile.domain.lecturer;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LecturerRepository extends CrudRepository<Lecturer, UUID> {

}
