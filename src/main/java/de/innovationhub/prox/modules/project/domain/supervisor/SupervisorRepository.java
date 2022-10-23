package de.innovationhub.prox.modules.project.domain.supervisor;

import de.innovationhub.prox.modules.commons.domain.DomainComponent;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

@DomainComponent
public interface SupervisorRepository extends CrudRepository<Supervisor, UUID> {
}
