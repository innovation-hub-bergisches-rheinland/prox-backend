package de.innovationhub.prox.profile.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

  private String vita;

  private String headquarter;

  @Builder.Default
  private List<String> quarters = new ArrayList<>();

  @Builder.Default
  private Map<SocialMedia, String> socialMediaHandles = new HashMap<>();
}
