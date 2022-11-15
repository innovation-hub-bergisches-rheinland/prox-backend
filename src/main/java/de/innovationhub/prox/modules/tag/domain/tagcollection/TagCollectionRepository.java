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
          where tc.id IN (
            select tc2.id from TagCollection tc2
            join tc2.tags t2
            where t2.tagName IN ?1
          )
          and t.tagName NOT IN ?1
          group by t.id
          order by count(t.id) desc
      """)
  List<Tag> findCommonUsedTagsWith(Collection<String> givenTags, Pageable pageable);
}
