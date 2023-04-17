package de.innovationhub.prox.modules.organization.application.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.organization.OrganizationFixtures;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetOrganizationTagsHandlerTest {
  OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  AuthenticationFacade authentication = mock(AuthenticationFacade.class);
  TagCollectionFacade tagCollectionFacade = mock(TagCollectionFacade.class);
  SetOrganizationTagsHandler handler = new SetOrganizationTagsHandler(organizationRepository, authentication, tagCollectionFacade);

  @Test
  void shouldThrowWhenOrganizationNotFound() {
    when(organizationRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), List.of()));
  }

  @Test
  void shouldSetTags() {
    var org = OrganizationFixtures.ACME_LTD;
    var tags = Instancio.ofList(TagDto.class).size(3).create();
    var tagIds = tags.stream().map(TagDto::id).toList();
    var tagCollectionId = org.getTagCollectionId();

    when(organizationRepository.findById(any())).thenReturn(Optional.of(org));
    when(authentication.currentAuthenticatedId()).thenReturn(OrganizationFixtures.ACME_ADMIN);
    when(tagCollectionFacade.setTagCollection(any(), any())).thenReturn(new TagCollectionDto(tagCollectionId, tags));

    handler.handle(org.getId(), tagIds);

    verify(organizationRepository).save(assertArg(o -> assertThat(o.getTagCollectionId()).isEqualTo(tagCollectionId)));
    verify(tagCollectionFacade).setTagCollection(eq(tagCollectionId), assertArg(t -> assertThat(t).containsExactlyElementsOf(tagIds)));
  }
}