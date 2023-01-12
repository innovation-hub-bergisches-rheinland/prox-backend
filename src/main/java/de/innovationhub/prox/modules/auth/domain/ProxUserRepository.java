package de.innovationhub.prox.modules.auth.domain;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxUserRepository extends JpaRepository<ProxUser, UUID> {

}
