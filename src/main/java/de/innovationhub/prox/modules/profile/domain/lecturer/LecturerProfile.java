package de.innovationhub.prox.modules.profile.domain.lecturer;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Embeddable
public class LecturerProfile {
  private String affiliation;
  private String subject;

  @Column(columnDefinition = "TEXT")
  private String vita;

  @Builder.Default
  @ElementCollection
  @Column(length = 1023)
  private List<String> publications = new ArrayList<>();

  private String room;
  // Sprechstunde
  // TODO: maybe "office hours" is a better title
  private String consultationHour;
  private String email;
  private String telephone;
  private String homepage;

  // The website of the university with personal information.
  // Some lecturers have filed most of their information there and don't wan't to duplicate it.
  // At the time of writing this comment, we do not have a shared IDP (Identity Provider) from which
  // we can fetch these information from, therefore we are just storing the URL here.
  private String collegePage;
}
