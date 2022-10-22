package de.innovationhub.prox.modules.tag.domain.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, UUID> {

  Optional<Tag> getByTag(String tag);

  List<Tag> getByIdIn(Collection<UUID> ids);

  default List<Tag> fetchOrCreateTags(Collection<String> tags) {
    List<Tag> existingTags = this.findAllByTagIn(tags);
    List<String> notExistingTags = tags.stream()
        .filter(strTag -> existingTags.stream().noneMatch(t -> t.getTag().equalsIgnoreCase(strTag)))
        .toList();
    List<Tag> createdTags = new ArrayList<>();
    for (var tag : notExistingTags) {
      createdTags.add(Tag.createNew(tag));
    }
    if (!createdTags.isEmpty()) {
      this.saveAll(createdTags);
    }

    return Stream.concat(existingTags.stream(), createdTags.stream()).toList();
  }

  boolean existsByTag(String tag);

  List<Tag> findAllByTagIn(Collection<String> tags);

  @Query("SELECT t FROM Tag t WHERE lower(t.tag) LIKE concat('%', lower(?1), '%')")
  List<Tag> findMatching(String tag);
}
