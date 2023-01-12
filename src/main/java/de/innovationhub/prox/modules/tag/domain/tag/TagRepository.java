package de.innovationhub.prox.modules.tag.domain.tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, UUID> {
  Optional<Tag> getByTagName(String tag);

  List<Tag> getByIdIn(Collection<UUID> ids);

  boolean existsByTagName(String tag);

  List<Tag> findAllByTagNameInIgnoreCase(Collection<String> tags);

  @Query("SELECT t FROM Tag t WHERE lower(t.tagName) LIKE concat('%', lower(?1), '%')")
  List<Tag> findMatching(String tag);
}
