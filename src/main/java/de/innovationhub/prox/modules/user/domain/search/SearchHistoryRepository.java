package de.innovationhub.prox.modules.user.domain.search;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, UUID> {

  Optional<SearchHistory> findByUserId(UUID userId);

  default SearchHistory findByUserIdOrCreate(UUID userId) {
    return findByUserId(userId).orElse(SearchHistory.create(userId));
  }
}
