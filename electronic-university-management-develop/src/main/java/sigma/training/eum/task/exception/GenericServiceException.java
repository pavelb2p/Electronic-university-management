package sigma.training.eum.task.exception;

/**
 * Generic Exception for unpredictable states
 */
public class GenericServiceException extends RuntimeException {
  public GenericServiceException(String message) {
    super(message);
  }
}
