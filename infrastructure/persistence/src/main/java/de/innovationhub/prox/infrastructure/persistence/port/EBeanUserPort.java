package de.innovationhub.prox.infrastructure.persistence.port;

import de.innovationhub.prox.commons.InfrastructureComponent;
import de.innovationhub.prox.infrastructure.persistence.mapper.UserMapper;
import de.innovationhub.prox.infrastructure.persistence.model.query.QUserEntity;
import de.innovationhub.prox.profile.user.User;
import de.innovationhub.prox.profile.user.UserPort;
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
    return qUser
      .id.eq(id)
      .findOneOrEmpty()
      .map(userMapper::toDomain);
  }
}
