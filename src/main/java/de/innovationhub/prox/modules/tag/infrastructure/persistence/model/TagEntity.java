package de.innovationhub.prox.modules.tag.infrastructure.persistence.model;

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
public class TagEntity extends BaseEntity {

  @Id
  private UUID id;

  @Column(unique = true, nullable = false)
  private String tag;

  public TagEntity(UUID id, String tag) {
    this.id = id;
    this.tag = tag;
  }
}
