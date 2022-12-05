package de.innovationhub.prox.modules.profile.domain.organization;

import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrganizationRepository extends PagingAndSortingRepository<Organization, UUID> {

}
