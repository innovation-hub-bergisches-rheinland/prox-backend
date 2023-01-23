package de.innovationhub.prox.modules.user.application.profile.dto;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class UserProfileDtoMapper {
  @Autowired
  private TagFacade tagFacade;
  @Autowired
  private StorageProvider storageProvider;


  @Mapping(target = "avatarUrl", source = ".", qualifiedByName = "retrieveAvatarUrl")
  @Mapping(target = "tags", source = "tags", qualifiedByName = "retrieveTags")
  public abstract UserProfileDto toDtoUserProfile(UserProfile userProfile);
  public abstract LecturerProfileDto toDtoLecturerProfile(LecturerProfile profile);
  public abstract LecturerProfileInformationDto toDtoLecturerProfileInformation(LecturerProfileInformation profileInformation);

  @Named("retrieveTags")
  public List<TagDto> retrieveTags(Collection<UUID> tagIds) {
    return tagFacade.getTags(tagIds)
        .stream().map(tag -> new TagDto(tag.id(), tag.tagName()))
        .toList();
  }

  @Named("retrieveAvatarUrl")
  public String retrieveAvatarUrl(UserProfile userProfile) {
    if(userProfile.getAvatarKey() == null) {
      return null;
    }
    return storageProvider.buildUrl(userProfile.getAvatarKey());
  }
}
