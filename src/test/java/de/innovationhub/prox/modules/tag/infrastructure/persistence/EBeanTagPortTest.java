package de.innovationhub.prox.modules.tag.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.tag.domain.tag.Tag;
import de.innovationhub.prox.modules.tag.infrastructure.persistence.model.query.QTagEntity;
import io.ebean.DB;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EBeanTagPortTest {

  EBeanTagPort tagPort = new EBeanTagPort();

  @BeforeAll
  static void setup() {
    DB.saveAll(TagEntities.TAGS);
  }

  @Test
  void save() {
    var tag = new Tag(UUID.randomUUID(), "milk");

    var saved = tagPort.save(tag);

    assertThat(saved).isNotNull();

    var found = new QTagEntity().id.eq(tag.getId()).findOneOrEmpty();

    assertThat(found).isPresent();
  }

  @Test
  void getByTag() {
    assertThat(tagPort.getByTag("duff")).isPresent();
  }

  @Test
  void existsByTag() {
    assertThat(tagPort.existsByTag("duff")).isTrue();
  }
}
