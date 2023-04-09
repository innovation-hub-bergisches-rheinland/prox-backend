package de.innovationhub.prox.modules.user.application.profile;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDtoMapper;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindAllLecturersByIdsHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindAllUserProfilesByIdsHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindLecturersWithAnyTagsHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindUserProfileHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.SearchUserHandler;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class UserProfileFacadeImpl implements UserProfileFacade {

  private final FindUserProfileHandler findUserProfileHandler;
  private final FindAllUserProfilesByIdsHandler findAllUserProfilesByIdsHandler;
  private final SearchUserHandler searchUserHandler;
  private final FindLecturersWithAnyTagsHandler findLecturersWithAnyTagsHandler;
  private final FindAllLecturersByIdsHandler findAllLecturersByIdsHandler;
  private final UserProfileDtoMapper userProfileDtoMapper;

  @Override
  @Cacheable(CacheConfig.USER_PROFILE)
  public Optional<UserProfileDto> getByUserId(UUID id) {
    return findUserProfileHandler.handle(id)
        .map(userProfileDtoMapper::toDtoUserProfile);
  }

  @Override
  @Cacheable(CacheConfig.USER_PROFILE)
  public List<UserProfileDto> findByUserId(List<UUID> ids) {
    return findAllUserProfilesByIdsHandler.handle(ids)
        .stream()
        .map(userProfileDtoMapper::toDtoUserProfile)
        .toList();
  }

  @Override
  @Cacheable(CacheConfig.USER_PROFILE)
  public List<UserProfileDto> search(String query) {
    return searchUserHandler.handle(query, Pageable.unpaged())
        .map(userProfileDtoMapper::toDtoUserProfile)
        .toList();
  }

  @Override
  public List<UserProfileDto> findLecturersWithAnyTags(List<UUID> tags) {
    return findLecturersWithAnyTagsHandler.handle(tags)
        .map(userProfileDtoMapper::toDtoUserProfile)
        .toList();
  }

  @Override
  public List<UserProfileDto> findLecturersByIds(Collection<UUID> ids) {
    return findAllLecturersByIdsHandler.handle(ids).stream()
        .map(userProfileDtoMapper::toDtoUserProfile)
        .toList();
  }
}
