package de.innovationhub.prox.modules.tag.domain.tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepository extends PagingAndSortingRepository<Tag, UUID>, CrudRepository<Tag, UUID> {

  List<Tag> findAllByTagNameInIgnoreCase(Collection<String> tags);

  @Query("SELECT t FROM Tag t LEFT JOIN t.aliases ta WHERE lower(t.tagName) LIKE concat('%', lower(?1), '%') or lower(ta) LIKE concat('%', lower(?1), '%')")
  Page<Tag> findMatching(String tag, Pageable pageable);
}
