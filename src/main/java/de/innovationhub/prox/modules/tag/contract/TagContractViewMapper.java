package de.innovationhub.prox.modules.tag.contract;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.domain.tagcollection.TagCollection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TagContractViewMapper {
  @Mapping(target = "tags", source = "tags")
  TagCollectionView toView(TagCollection tagCollection);

  @Mapping(target = ".", source = "tag.tagName")
  String toString(Tag tag);
}
