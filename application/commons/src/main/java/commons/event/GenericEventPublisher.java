package commons.event;

/**
 * Generic Event Publisher interface. Publishes events once they have been happened. Publishing can
 * be done synchronously or asynchronously using a in-process message bus or a remote message broker
 * that depends on the implementation.
 *
 * @param <T> Event type to publish
 */
public interface GenericEventPublisher<T extends Event> {

  void publish(T event);
}
