package de.innovationhub.prox.modules.profile;

import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import java.util.UUID;

public class OrganizationFixtures {
  public static UUID ACME_ADMIN = UUID.fromString("00000000-0000-0000-0000-000000000001");
  public static Organization ACME_LTD = Organization.create("ACME Ltd", new UserAccount(ACME_ADMIN));
}
