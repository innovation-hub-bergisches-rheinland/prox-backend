package de.innovationhub.prox.modules.profile.domain.organization;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, UUID> {

  @Override
  @Query("select o from Organization o order by o.name desc")
  Iterable<Organization> findAll();
}
