package de.innovationhub.prox.modules.tag.infrastructure.persistence;

import de.innovationhub.prox.modules.tag.infrastructure.persistence.model.TagEntity;
import java.util.List;
import java.util.UUID;

public class TagEntities {

  public static TagEntity DUFF = new TagEntity(UUID.randomUUID(), "duff");
  public static TagEntity BEER = new TagEntity(UUID.randomUUID(), "beer");
  public static TagEntity WATER = new TagEntity(UUID.randomUUID(), "water");

  public static List<TagEntity> TAGS = List.of(DUFF, BEER, WATER);
}
