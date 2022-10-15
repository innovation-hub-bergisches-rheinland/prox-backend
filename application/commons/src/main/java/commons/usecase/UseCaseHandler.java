package commons.usecase;

/**
 * Interface for marking use case handlers.
 *
 * @param <E> Response Type of the handler
 * @param <T> Use Case type
 */
public interface UseCaseHandler<E, T extends UseCase> {

  /**
   * Handles the use case
   *
   * @param useCase usecase
   * @return response
   */
  E handle(T useCase);
}
