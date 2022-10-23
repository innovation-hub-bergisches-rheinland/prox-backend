package de.innovationhub.prox.modules.project.domain.partner;

import de.innovationhub.prox.modules.commons.domain.DomainComponent;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

@DomainComponent
public interface PartnerRepository extends CrudRepository<Partner, UUID> {

}
