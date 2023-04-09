package de.innovationhub.prox.modules.tag.contract;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@ApplicationComponent
public interface TagFacade {

  List<String> getTagsAsString(Collection<UUID> id);

  List<TagDto> getTags(Collection<UUID> id);

  List<TagDto> getTagsByName(Collection<String> id);
}
