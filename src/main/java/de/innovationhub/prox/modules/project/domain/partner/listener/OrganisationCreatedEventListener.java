package de.innovationhub.prox.modules.project.domain.partner.listener;

import de.innovationhub.prox.modules.commons.domain.DomainComponent;
import de.innovationhub.prox.modules.profile.domain.organization.events.OrganizationCreated;
import de.innovationhub.prox.modules.project.domain.partner.Partner;
import de.innovationhub.prox.modules.project.domain.partner.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@DomainComponent
@RequiredArgsConstructor
public class OrganisationCreatedEventListener {
  private final PartnerRepository partnerRepository;

  @EventListener
  public void onOrganizationCreated(OrganizationCreated organizationCreated) {
    var partner = Partner.create(organizationCreated.name());
    partnerRepository.save(partner);
  }
}
