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
        select t from Tag t
        where t.id in (
          select t1.id from TagCollection tc
          join tc.tags t1
        )
        group by t.id
        having count(t.id) > 0
        order by count(t.id) desc
      """)
  List<Tag> findPopularTags(Pageable pageable);

  @Query("""
        select t from Tag t
        where t.id in (
          select t1.id from TagCollection tc1
          join tc1.tags t1
          where tc1.id in (
            select tc.id from TagCollection tc
            join tc.tags t
            where t.tagName in ?1
          )
          and t1.tagName not in ?1
        )
        group by t.id
        having count(t.id) > 0
        order by count(t.id) desc
      """)
  List<Tag> findCommonUsedTagsWith(Collection<String> givenTags, Pageable pageable);
}
