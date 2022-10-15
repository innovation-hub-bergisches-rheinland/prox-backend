package de.innovationhub.prox.infrastructure.persistence.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import java.time.Instant;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity extends Model {

  @Version
  long version;

  @WhenCreated
  Instant whenCreated;

  @WhenModified
  Instant whenModified;
}
