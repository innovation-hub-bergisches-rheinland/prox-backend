package de.innovationhub.prox.infrastructure.persistence.model;

import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LecturerEntity extends BaseEntity {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String name;

  @OneToOne(optional = false)
  private UUID userId;

  @ElementCollection
  private List<UUID> tags;

  private String affiliation;
  private String subject;
  private String vita;
  @ElementCollection
  private List<String> publications;
  private String room;
  private String consultationHour;
  private String email;
  private String telephone;
  private String homepage;
  private String collegePage;

  public LecturerEntity(UUID id, String name, UUID userId) {
    this.id = id;
    this.name = name;
    this.userId = userId;
  }
}
