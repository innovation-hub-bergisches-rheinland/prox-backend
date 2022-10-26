package de.innovationhub.prox.modules.commons.contract;

/**
 * Marker interface for an integration event.
 * Integration events are used to communicate between bounded contexts.
 * Integration events are published by exactly one producer and may be consumed by an arbitrary
 * number of consumers
 */
public interface IntegrationEvent {

}
