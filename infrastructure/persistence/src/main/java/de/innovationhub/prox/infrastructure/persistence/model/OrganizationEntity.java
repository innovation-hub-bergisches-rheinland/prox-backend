package de.innovationhub.prox.infrastructure.persistence.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationEntity extends BaseEntity {
  @Id
  private UUID id;

  @Column(nullable = false)
  private String name;

  private String foundingDate;

  private String numberOfEmployees;

  private String homepage;

  private String contactEmail;

  private String vita;

  private String headquarter;

  @OneToMany(cascade = CascadeType.ALL)
  private List<OrganizationMembershipEntity> memberships;

  @ElementCollection
  private List<String> quarters;

  @ElementCollection
  private Map<String, String> socialMedia;

  @ElementCollection
  private List<UUID> tags;

  public OrganizationEntity(UUID id, String name) {
    this.id = id;
    this.name = name;
  }
}
