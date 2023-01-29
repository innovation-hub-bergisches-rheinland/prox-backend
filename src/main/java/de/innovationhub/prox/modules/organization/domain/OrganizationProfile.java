package de.innovationhub.prox.modules.organization.domain;

import de.innovationhub.prox.config.PersistenceConfig;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class OrganizationProfile {

  @Size(max = 255)
  private String foundingDate;

  @Size(max = 255)
  private String numberOfEmployees;

  @Size(max = 255)
  private String homepage;

  /*
   * EMail validation is disabled because it is a valid use case to specify multiple EMail addresses
   * which we currently do not offer. Since the Email itself does not have any clear
   * semantics at the moment we simply allow any kind of string. For the future it is thinkable to
   * deprecate the organization-wide email address and simple list members with their EMail address
   * as representatives
   */
  @Size(max = 255)
  // @Email
  private String contactEmail;

  @Column(columnDefinition = "TEXT")
  private String vita;

  private String headquarter;

  private String quarters;

  @Builder.Default
  @ElementCollection
  @CollectionTable(schema = PersistenceConfig.ORGANIZATION_SCHEMA)
  private Map<SocialMedia, String> socialMediaHandles = new HashMap<>();
}
