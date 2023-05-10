package de.innovationhub.prox.modules.user.domain.search;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchPreferencesRepository extends JpaRepository<SearchPreferences, UUID> {

}
