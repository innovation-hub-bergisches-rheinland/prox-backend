package de.innovationhub.prox;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class ClearDatabaseExtension implements BeforeEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    var flyway = SpringExtension.getApplicationContext(context).getBean(Flyway.class);
    flyway.clean();
    flyway.migrate();
  }
}
