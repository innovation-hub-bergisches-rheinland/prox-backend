package de.innovationhub.prox.modules.user.application.search.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.user.application.search.dto.CreateSearchPreferencesRequest;
import de.innovationhub.prox.modules.user.application.search.dto.SearchPreferencesDtoMapper;
import de.innovationhub.prox.modules.user.contract.search.dto.SearchPreferencesDto;
import de.innovationhub.prox.modules.user.domain.search.LecturerSearch;
import de.innovationhub.prox.modules.user.domain.search.OrganizationSearch;
import de.innovationhub.prox.modules.user.domain.search.ProjectSearch;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferences;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferencesRepository;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateSearchPreferencesHandler {
  private final SearchPreferencesRepository searchPreferencesRepository;
  private final SearchPreferencesDtoMapper searchPreferencesDtoMapper;

  @Transactional
  public SearchPreferencesDto handle(UUID userId, CreateSearchPreferencesRequest request) {
    Objects.requireNonNull(userId);
    Objects.requireNonNull(request);

    var searchPreferences = searchPreferencesRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("Search preferences does not exist for user"));

    searchPreferences.update(
        new ProjectSearch(
            request.projectSearch().enabled(),
            request.projectSearch().moduleTypes(),
            request.projectSearch().disciplines()
        ),
        new OrganizationSearch(
            request.organizationSearch().enabled()
        ),
        new LecturerSearch(
            request.lecturerSearch().enabled()
        )
    );
    searchPreferences = searchPreferencesRepository.save(searchPreferences);
    return searchPreferencesDtoMapper.toDto(searchPreferences);
  }
}
