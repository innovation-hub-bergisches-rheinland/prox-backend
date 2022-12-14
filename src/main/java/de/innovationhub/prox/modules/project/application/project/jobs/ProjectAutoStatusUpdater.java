package de.innovationhub.prox.modules.project.application.project.jobs;


import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectAutoStatusUpdater {
  private final ProjectRepository projectRepository;

  public ProjectAutoStatusUpdater(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Scheduled(cron = "0 0 0 * * *")
  void run() {
    projectRepository.findStartedOfferedProjects().forEach(project -> {
      project.start();
      projectRepository.save(project);
    });
    projectRepository.findFinishedRunningProjects().forEach(project -> {
      project.complete();
      projectRepository.save(project);
    });
  }
}
