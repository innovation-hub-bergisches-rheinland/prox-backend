package de.innovationhub.prox.modules.project;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ProjectIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  ProjectRepository projectRepository;
  @Autowired
  DisciplineRepository disciplineRepository;
  @Autowired
  ModuleTypeRepository moduleTypeRepository;

  @BeforeEach
  void setUp() {
    disciplineRepository.saveAll(DisciplineFixtures.ALL);
    moduleTypeRepository.saveAll(ModuleTypeFixtures.ALL);
  }
}
