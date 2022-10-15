package de.innovationhub.prox;

import commons.ApplicationComponent;
import de.innovationhub.prox.commons.DomainComponent;
import de.innovationhub.prox.commons.InfrastructureComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {
  DomainComponent.class,
  ApplicationComponent.class,
  InfrastructureComponent.class
}))
public class ProxApplication {
  public static void main(String[] args) {
    SpringApplication.run(ProxApplication.class, args);
  }
}
