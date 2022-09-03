package de.innovationhub.prox.project.project;

import java.time.LocalDate;
import lombok.Data;

/**
 * Period where a project is actively worked on. It is meant to be defined vaguely, as sometimes a
 * lecturer might not have the capacity to carry out a project in a specific time frame. But we must
 * have some kind of time frame to be able to accumulate the project's progress.
 */
@Data
public class TimeBox {

  private final LocalDate start;
  private final LocalDate end;

  public TimeBox(LocalDate start, LocalDate end) {
    this.start = start;
    this.end = end;
  }
}
