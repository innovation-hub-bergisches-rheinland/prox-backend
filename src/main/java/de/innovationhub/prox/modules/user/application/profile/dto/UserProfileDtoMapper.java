package de.innovationhub.prox.modules.user.application.profile.dto;

import de.innovationhub.prox.infra.aws.StorageProvider;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto.ContactInformationRequestDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.LecturerProfileDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class UserProfileDtoMapper {
  public static final UserProfileDtoMapper INSTANCE = Mappers.getMapper(UserProfileDtoMapper.class);

  @Autowired
  private TagCollectionFacade tagCollectionFacade;
  @Autowired
  private StorageProvider storageProvider;


  @Mapping(target = "avatarUrl", source = ".", qualifiedByName = "retrieveAvatarUrl")
  @Mapping(target = "tags", source = ".", qualifiedByName = "retrieveTags")
  @Mapping(target = "contact", source = "contactInformation")
  public abstract UserProfileDto toDtoUserProfile(UserProfile userProfile);
  public abstract LecturerProfileDto toDtoLecturerProfile(LecturerProfile profile);
  public abstract LecturerProfileInformationDto toDtoLecturerProfileInformation(LecturerProfileInformation profileInformation);

  @Named("retrieveTags")
  public List<TagDto> retrieveTags(UserProfile userProfile) {
    var tagCollectionId = userProfile.getTagCollectionId();
    if(tagCollectionId == null) return List.of();

    return tagCollectionFacade.getTagCollection(userProfile.getTagCollectionId()).map(
        TagCollectionDto::tags).orElse(List.of());
  }

  @Named("retrieveAvatarUrl")
  public String retrieveAvatarUrl(UserProfile userProfile) {
    if(userProfile.getAvatarKey() == null) {
      return null;
    }
    return storageProvider.buildUrl(userProfile.getAvatarKey());
  }

  public abstract ContactInformation toContactInformation(ContactInformationRequestDto contactInformationDto);
}
