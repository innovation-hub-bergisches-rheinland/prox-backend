package commons.usecase;

/**
 * Marker interface for a use case handler without a response
 *
 * @param <T> Use Case type
 */
public interface VoidUseCaseHandler<T extends VoidUseCase> {

  /**
   * Handles the use case
   *
   * @param voidUseCase
   */
  void handle(T voidUseCase);
}
