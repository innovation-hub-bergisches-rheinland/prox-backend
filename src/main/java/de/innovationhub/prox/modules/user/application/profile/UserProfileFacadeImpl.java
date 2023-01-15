package de.innovationhub.prox.modules.user.application.profile;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindAllUserProfilesByIdsHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindUserProfileHandler;
import de.innovationhub.prox.modules.user.contract.lecturer.UserProfileFacade;
import de.innovationhub.prox.modules.user.contract.lecturer.UserProfileView;
import de.innovationhub.prox.modules.user.contract.lecturer.UserProfileViewMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class UserProfileFacadeImpl implements UserProfileFacade {

  private final FindUserProfileHandler findUserProfileHandler;
  private final FindAllUserProfilesByIdsHandler findAllUserProfilesByIdsHandler;
  private final UserProfileViewMapper userProfileViewMapper;

  @Override
  @Cacheable(CacheConfig.LECTURERS)
  public Optional<UserProfileView> get(UUID id) {
    return findUserProfileHandler.handle(id)
        .map(userProfileViewMapper::toView);
  }

  @Override
  @Cacheable(CacheConfig.LECTURERS)
  public List<UserProfileView> findByIds(List<UUID> ids) {
    return userProfileViewMapper.toViewList(
        findAllUserProfilesByIdsHandler.handle(ids)
    );
  }
}
