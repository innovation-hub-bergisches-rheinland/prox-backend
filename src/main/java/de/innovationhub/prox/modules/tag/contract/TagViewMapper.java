package de.innovationhub.prox.modules.tag.contract;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface TagViewMapper {
  TagView toView(Tag tag);
  List<TagView> toViewList(List<Tag> tags);
}
