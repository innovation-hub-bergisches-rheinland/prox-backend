package de.innovationhub.prox.modules.tag.domain.tagcollection;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagCollectionRepository extends JpaRepository<TagCollection, UUID> {

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


  // I hate Hibernate, JPA and every ORM in the whole world. I can't get this query to work with
  // query expressions. Neither with JPQL nor with native SQL. I don't know why, but it just
  // doesn't work. I tried everything. Hibernate throws a NotYetImplementedException with JPQL
  // and a native query throws ConverterNotFoundException. I don't know why.
  // I'm sorry for this solution, but I don't know how to write it in a simpler way under this conditions.
  // Why do I have to learn Shenannigans like this? Why can't I just write a simple query? I know
  // how to write a query in SQL. Why do I have to learn this stupid query language and find in the
  // thousands sites of documentation how it will be mapped on my entities? It could be so simple.
  // *sigh*
  @Query(value = """
      select distinct t.*, count(t.id) as cnt from prox_tag.tag t
      where t.id in (
        select t2.id from prox_tag.tag_collection_tags tc1
        join prox_tag.tag t2 on t2.id = tc1.tags_id
        where tc1.tag_collection_id in (
          select tc.tag_collection_id from prox_tag.tag_collection_tags tc
          join prox_tag.tag t3 on t3.id = tc.tags_id
          where t3.tag_name in ?1
        )
        and t2.tag_name not in ?1
      )
      group by t.id
      having count(t.id) > 0
      order by cnt desc
        """, nativeQuery = true)
  List<Object[]> findCommonUsedTagsWithObj(Collection<String> givenTags, Pageable pageable);

  default List<Tag> findCommonUsedTagsWith(Collection<String> givenTags, Pageable pageable) {
    return findCommonUsedTagsWithObj(givenTags, pageable).stream()
        .map(o -> new Tag((UUID) o[0], (String) o[1]))
        .collect(Collectors.toList());
  }
}
