package de.innovationhub.prox.modules.tag.contract;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@ApplicationComponent
public interface TagFacade {
  List<String> getTags(Collection<UUID> id);
  List<TagView> getTagsByName(Collection<String> id);
}
