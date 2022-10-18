package de.innovationhub.prox.modules.profile.infrastructure.persistence.model;

import de.innovationhub.prox.modules.commons.infrastructure.persistence.BaseEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  public UserEntity(UUID id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }
}
