package de.innovationhub.prox.modules.user.domain.star;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarCollectionRepository extends JpaRepository<StarCollection, UUID> {
  Optional<StarCollection> findByUserId(UUID user);
}
