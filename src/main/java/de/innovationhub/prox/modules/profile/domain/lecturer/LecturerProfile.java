package de.innovationhub.prox.modules.profile.domain.lecturer;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturerProfile {

  private String affiliation;
  private String subject;
  private String vita;
  @Builder.Default
  private List<String> publications = new ArrayList<>();

  private String room;
  // Sprechstunde
  // TODO: maybe "office hours" is a better name
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
