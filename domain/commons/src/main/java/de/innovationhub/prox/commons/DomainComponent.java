package de.innovationhub.prox.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Marks the class as a domain component that will be picked up by the IoC container and provides
 * Dependency Injection for it. This can be useful for e.g. Domain Services
 */
public @interface DomainComponent {

}
