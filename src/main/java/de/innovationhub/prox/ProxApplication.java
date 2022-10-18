package de.innovationhub.prox;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.domain.DomainComponent;
import de.innovationhub.prox.modules.commons.infrastructure.InfrastructureComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    includeFilters =
        @ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            value = {
              DomainComponent.class,
              ApplicationComponent.class,
              InfrastructureComponent.class
            }))
@Slf4j
public class ProxApplication {
  public static void main(String[] args) {
    SpringApplication.run(ProxApplication.class, args);
  }
}
