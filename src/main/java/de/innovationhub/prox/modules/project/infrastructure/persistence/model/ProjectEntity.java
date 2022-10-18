package de.innovationhub.prox.modules.project.infrastructure.persistence.model;

import de.innovationhub.prox.modules.commons.infrastructure.persistence.BaseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity extends BaseEntity {

  @Id
  private UUID id;

  @Column(nullable = false)
  private UUID creator;

  private UUID organization;

  @Column(nullable = false)
  private String title;

  private String summary;
  private String description;
  private String requirement;

  @ElementCollection
  private List<String> disciplines;

  @ElementCollection
  private List<String> modules;

  @Embedded
  private ProjectStatusEmbeddable status;

  private LocalDate timeBoxStart;
  private LocalDate timeBoxEnd;

  @ElementCollection
  private List<UUID> supervisors;

  @ElementCollection
  private List<UUID> tags;
}
