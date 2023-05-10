package de.innovationhub.prox.modules.user.application.search.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.search.dto.SearchPreferencesDtoMapper;
import de.innovationhub.prox.modules.user.contract.search.dto.SearchPreferencesDto;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferencesRepository;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindSearchPreferencesByUserIdHandler {
  private final SearchPreferencesDtoMapper searchPreferencesDtoMapper;
  private final SearchPreferencesRepository searchPreferencesRepository;

  public Optional<SearchPreferencesDto> handle(UUID userId) {
    Objects.requireNonNull(userId);
    return searchPreferencesRepository.findByUserId(userId)
        .map(searchPreferencesDtoMapper::toDto);
  }
}
