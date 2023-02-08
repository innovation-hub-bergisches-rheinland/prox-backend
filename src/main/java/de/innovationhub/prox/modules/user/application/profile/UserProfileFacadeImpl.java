package de.innovationhub.prox.modules.user.application.profile;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindAllUserProfilesByIdsHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindUserProfileHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.SearchUserHandler;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileView;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileViewMapper;
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
  private final UserProfileViewMapper userProfileViewMapper;

  @Override
  @Cacheable(CacheConfig.USER_PROFILE)
  public Optional<UserProfileView> getByUserId(UUID id) {
    return findUserProfileHandler.handle(id)
        .map(userProfileViewMapper::toView);
  }

  @Override
  @Cacheable(CacheConfig.USER_PROFILE)
  public List<UserProfileView> findByUserId(List<UUID> ids) {
    return userProfileViewMapper.toViewList(
        findAllUserProfilesByIdsHandler.handle(ids)
    );
  }

  @Override
  @Cacheable(CacheConfig.USER_PROFILE)
  public List<UserProfileView> search(String query) {
    return searchUserHandler.handle(query, Pageable.unpaged())
        .map(userProfileViewMapper::toView)
        .toList();
  }
}
