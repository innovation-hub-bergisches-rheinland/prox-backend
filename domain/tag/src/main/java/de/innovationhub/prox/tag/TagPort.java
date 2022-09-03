package de.innovationhub.prox.tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagPort {
  Tag save(Tag tag);
  Optional<Tag> getByTag(String tag);
  boolean existsByTag(String tag);
  List<Tag> findMatching(String tag);
  List<Tag> findCommon(Collection<String> tags);
}
