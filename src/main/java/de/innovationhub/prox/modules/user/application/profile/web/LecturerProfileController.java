package de.innovationhub.prox.modules.user.application.profile.web;

import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDtoMapper;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.FindAllLecturersHandler;
import de.innovationhub.prox.modules.user.application.profile.usecase.queries.SearchLecturerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lecturers")
@RequiredArgsConstructor
public class LecturerProfileController {
  private final FindAllLecturersHandler findAll;
  private final SearchLecturerHandler searchLecturer;
  private final UserProfileDtoMapper dtoMapper;

  @GetMapping(produces = "application/json")
  public ResponseEntity<Page<UserProfileDto>> getAllLecturers(Pageable pageable) {
    var lecturers = findAll.handle(pageable);
    return ResponseEntity.ok(lecturers.map(dtoMapper::toDtoUserProfile));
  }

  @GetMapping(value = "search/filter", produces = "application/json")
  public ResponseEntity<Page<UserProfileDto>> search(@RequestParam("q") String query, Pageable pageable) {
    var lecturers = searchLecturer.handle(query, pageable);
    return ResponseEntity.ok(lecturers.map(dtoMapper::toDtoUserProfile));
  }
}
