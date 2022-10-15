package de.innovationhub.prox.infrastructure.persistence;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;
import java.io.IOException;

public class MigrationGenerator {
  /**
   * Generate the next "DB schema DIFF" migration.
   */
  public static void main(String[] args) throws IOException {
    DbMigration dbMigration = DbMigration.create();
    dbMigration.setPlatform(Platform.POSTGRES);
    // TODO: We need to integrate migrations with our application configuration.
    //  Either by bundling the persistence resource path or changing the path here.
    dbMigration.setPathToResources("infrastructure/persistence/src/main/resources");

    dbMigration.generateMigration();
  }
}
