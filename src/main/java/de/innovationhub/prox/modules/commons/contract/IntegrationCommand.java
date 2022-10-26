package de.innovationhub.prox.modules.commons.contract;

/**
 * Marker interface for an integration command.
 * Integration commands are the only type of direct communication between bounded contexts.
 * They are used to express a functionality that is hard to express with events, and delegate a
 * specific task to another bounded context.
 * Integration commands are published by an arbitrary number of producers and must be consumed by
 * exactly one consumer.
 */
public interface IntegrationCommand {

}
