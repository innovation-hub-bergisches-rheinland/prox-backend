package de.innovationhub.prox;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.commons.stereotypes.DomainComponent;
import de.innovationhub.prox.commons.stereotypes.InfrastructureComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
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
@ConfigurationPropertiesScan
@Slf4j
public class ProxApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProxApplication.class, args);
  }
}
