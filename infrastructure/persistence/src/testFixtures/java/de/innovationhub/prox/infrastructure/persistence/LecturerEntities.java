package de.innovationhub.prox.infrastructure.persistence;

import de.innovationhub.prox.infrastructure.persistence.model.LecturerEntity;
import java.util.List;
import java.util.UUID;

public class LecturerEntities {

  public static LecturerEntity HOMER = new LecturerEntity(
    UUID.randomUUID(),
    "Homer Simpson",
    UUID.randomUUID()
  );

  public static List<LecturerEntity> LECTURERS = List.of(HOMER);

  static {
    HOMER.setTags(List.of(UUID.randomUUID()));
    HOMER.setAffiliation("2022");
    HOMER.setVita("Lorem Ipsum");
    HOMER.setTelephone("555-12345-6789");
    HOMER.setEmail("homer.simpson@example.com");
    HOMER.setRoom(null);
    HOMER.setHomepage("www.example.com");
  }
}
