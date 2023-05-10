package de.innovationhub.prox.modules.user.domain.search;

import de.innovationhub.prox.config.PersistenceConfig;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(schema = PersistenceConfig.USER_SCHEMA)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
public class LecturerSearch {
  @Id
  private UUID id = UUID.randomUUID();

  private Boolean enabled = false;

  public LecturerSearch(Boolean enabled) {
    this.enabled = enabled;
  }
}
