package de.innovationhub.prox.modules.commons.application.usecase;

/**
 * Marker interface for a use case handler without a response
 *
 * @param <USECASE> Use Case type
 */
public interface VoidUseCaseHandler<USECASE extends VoidUseCase> {

  /**
   * Handles the use case
   *
   * @param voidUseCase
   */
  void handle(USECASE voidUseCase);
}
