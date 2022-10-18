package de.innovationhub.prox.modules.profile.infrastructure.persistence;

import de.innovationhub.prox.modules.commons.infrastructure.InfrastructureComponent;
import de.innovationhub.prox.modules.profile.domain.user.User;
import de.innovationhub.prox.modules.profile.domain.user.UserPort;
import de.innovationhub.prox.modules.profile.infrastructure.persistence.mapper.UserMapper;
import de.innovationhub.prox.modules.profile.infrastructure.persistence.model.query.QUserEntity;
import java.util.Optional;
import java.util.UUID;

@InfrastructureComponent
public class EBeanUserPort implements UserPort {

  private final QUserEntity qUser = new QUserEntity();
  private final UserMapper userMapper = UserMapper.MAPPER;

  @Override
  public User save(User user) {
    var entity = userMapper.toPersistence(user);
    entity.save();
    return userMapper.toDomain(entity);
  }

  @Override
  public Optional<User> getById(UUID id) {
    return qUser.id.eq(id).findOneOrEmpty().map(userMapper::toDomain);
  }
}
