package de.innovationhub.prox.infrastructure.persistence.port;

import de.innovationhub.prox.commons.InfrastructureComponent;
import de.innovationhub.prox.infrastructure.persistence.mapper.LecturerMapper;
import de.innovationhub.prox.infrastructure.persistence.model.query.QLecturerEntity;
import de.innovationhub.prox.profile.lecturer.Lecturer;
import de.innovationhub.prox.profile.lecturer.LecturerPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@InfrastructureComponent
public class EBeanLecturerPort implements LecturerPort {

  private static final QLecturerEntity qLecturer = new QLecturerEntity();
  private static final LecturerMapper lecturerMapper = LecturerMapper.MAPPER;

  @Override
  public Lecturer save(Lecturer lecturer) {
    var entity = lecturerMapper.toPersistence(lecturer);
    entity.save();
    return lecturerMapper.toDomain(entity);
  }

  @Override
  public List<Lecturer> getAll() {
    return qLecturer
      .findList()
      .stream()
      .map(lecturerMapper::toDomain)
      .toList();
  }

  @Override
  public Optional<Lecturer> getById(UUID id) {
    return qLecturer
      .id.eq(id)
      .findOneOrEmpty()
      .map(lecturerMapper::toDomain);
  }

  @Override
  public boolean existsByUserId(UUID id) {
    return qLecturer
      .id.eq(id)
      .exists();
  }
}
