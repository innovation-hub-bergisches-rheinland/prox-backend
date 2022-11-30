package de.innovationhub.prox.modules.profile;

import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import java.util.List;
import java.util.UUID;

public class OrganizationFixtures {

  public static String ACME_ADMIN_ID_STR = "00000000-0000-0000-0000-000000000001";
  public static UUID ACME_ADMIN = UUID.fromString(ACME_ADMIN_ID_STR);
  public static Organization ACME_LTD = Organization.create("ACME Ltd",
      ACME_ADMIN);

  public static List<Organization> ALL = List.of(ACME_LTD);
}
