package de.innovationhub.prox.modules.user.application.search.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.search.dto.SetSearchPreferencesRequest;
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
public class SetSearchPreferencesHandler {

  private final SearchPreferencesRepository searchPreferencesRepository;
  private final SearchPreferencesDtoMapper searchPreferencesDtoMapper;

  @Transactional
  public SearchPreferencesDto handle(UUID userId, SetSearchPreferencesRequest request) {
    Objects.requireNonNull(userId);
    Objects.requireNonNull(request);

    var projectSearch = new ProjectSearch(
        request.projectSearch().enabled(),
        request.projectSearch().moduleTypes(),
        request.projectSearch().disciplines()
    );
    var organizationSearch = new OrganizationSearch(
        request.organizationSearch().enabled()
    );
    var lecturerSearch = new LecturerSearch(
        request.lecturerSearch().enabled()
    );

    var searchPreferences = searchPreferencesRepository.findByUserId(userId)
        .map(sp -> {
          sp.update(projectSearch, organizationSearch, lecturerSearch);
          return sp;
        })
        .orElseGet(() -> SearchPreferences.create(
            userId,
            projectSearch,
            organizationSearch,
            lecturerSearch
        ));

    searchPreferences = searchPreferencesRepository.save(searchPreferences);
    return searchPreferencesDtoMapper.toDto(searchPreferences);
  }
}
