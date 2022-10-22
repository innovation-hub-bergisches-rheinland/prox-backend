package de.innovationhub.prox.modules.tag.domain.tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, UUID> {

  Optional<Tag> getByTag(String tag);

  List<Tag> getByIdIn(Collection<UUID> ids);

  boolean existsByTag(String tag);

  List<Tag> findAllByTagIn(Collection<String> tags);

  @Query("SELECT t FROM Tag t WHERE lower(t.tag) LIKE concat('%', lower(?1), '%')")
  List<Tag> findMatching(String tag);
}
