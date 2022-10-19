package de.innovationhub.prox.modules.commons.application.event;

/**
 * Event Publisher interface. Publishes events once they have been happened. Publishing can be done
 * synchronously or asynchronously using a in-process message bus or a remote message broker that
 * depends on the implementation.
 */
public interface EventPublisher {

  <EVENT extends Event> void publish(EVENT event);
}
