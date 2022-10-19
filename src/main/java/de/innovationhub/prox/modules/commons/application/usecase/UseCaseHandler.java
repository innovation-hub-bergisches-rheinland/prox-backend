package de.innovationhub.prox.modules.commons.application.usecase;

/**
 * Interface for marking use case handlers.
 *
 * @param <RETURN>  Response Type of the handler
 * @param <USECASE> Use Case type
 */
public interface UseCaseHandler<RETURN, USECASE extends UseCase> {

  /**
   * Handles the use case
   *
   * @param useCase usecase
   * @return response
   */
  RETURN handle(USECASE useCase);
}
