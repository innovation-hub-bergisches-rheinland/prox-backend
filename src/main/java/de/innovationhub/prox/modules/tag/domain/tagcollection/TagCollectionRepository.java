package de.innovationhub.prox.modules.tag.domain.tagcollection;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagCollectionRepository extends CrudRepository<TagCollection, UUID> {

  @Query("""
        select t from TagCollection tc
        join tc.tags t
        group by t
        order by count(t) desc
      """)
  List<UUID> findPopularTags();

  @Query("""
        select t from TagCollection tc
        join tc.tags t
        where tc.id in :givenTags
        group by t
        order by count(t) desc
      """)
  List<UUID> findCommonUsedTagsWith(Collection<UUID> givenTags);
}
