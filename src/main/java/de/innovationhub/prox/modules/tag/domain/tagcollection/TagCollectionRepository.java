package de.innovationhub.prox.modules.tag.domain.tagcollection;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagCollectionRepository extends CrudRepository<TagCollection, UUID> {

  @Query("""
        select t from TagCollection tc
        join tc.tags t
        group by t
        order by count(t) desc
      """)
  List<Tag> findPopularTags(Pageable pageable);

  @Query("""
        select t from TagCollection tc
        join tc.tags t
        where t.tagName in :givenTags
        group by t
        order by count(t) desc
      """)
  List<Tag> findCommonUsedTagsWith(Collection<String> givenTags, Pageable pageable);
}
