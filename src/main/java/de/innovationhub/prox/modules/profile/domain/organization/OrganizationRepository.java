package de.innovationhub.prox.modules.profile.domain.organization;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, UUID> {

}
