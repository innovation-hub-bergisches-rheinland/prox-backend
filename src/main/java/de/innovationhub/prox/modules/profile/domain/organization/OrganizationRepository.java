package de.innovationhub.prox.modules.profile.domain.organization;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

  @Override
  @Query("select o from Organization o order by o.name desc")
  Page<Organization> findAll(Pageable pageable);
}
