package de.innovationhub.prox.modules.commons.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Marks the class as a application component that will be picked up by the IoC container and
 * provides Dependency Injection for it.
 */
public @interface ApplicationComponent {}
