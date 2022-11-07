package de.innovationhub.prox.modules.profile.application.lecturer.web;

import de.innovationhub.prox.modules.profile.application.lecturer.usecase.CreateLecturerHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.FindAllLecturersHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.FindLecturerHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.UpdateLecturerProfileHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.LecturerDtoAssembler;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.ReadLecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.UpdateLecturerDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lecturers")
@RequiredArgsConstructor
public class LecturerController {
  private final CreateLecturerHandler createLecturerHandler;
  private final FindAllLecturersHandler findAllLecturersHandler;
  private final FindLecturerHandler findLecturerHandler;
  private final UpdateLecturerProfileHandler updateLecturerProfileHandler;
  private final LecturerDtoAssembler lecturerDtoAssembler;

  @GetMapping
  public ResponseEntity<List<ReadLecturerDto>> getAll() {
    var dtoList = findAllLecturersHandler.handle().stream()
        .map(lecturerDtoAssembler::toDto)
        .toList();
    return ResponseEntity.ok(dtoList);
  }

  @GetMapping("{id}")
  public ResponseEntity<ReadLecturerDto> getById(@PathVariable("id") UUID id) {
    var dto = findLecturerHandler.handle(id)
        .map(lecturerDtoAssembler::toDto);
    if(dto.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(dto.get());
  }

  @PostMapping
  public ResponseEntity<ReadLecturerDto> create(
      @RequestBody CreateLecturerDto createLecturerDto
  ) {
    var lecturer = createLecturerHandler.handle(createLecturerDto);
    var dto = lecturerDtoAssembler.toDto(lecturer);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @PutMapping("{id}")
  public ResponseEntity<ReadLecturerDto> update(
      @PathVariable("id") UUID id,
      @RequestBody UpdateLecturerDto updateLecturerDto
  ) {
    var lecturer = updateLecturerProfileHandler.handle(id, updateLecturerDto);
    var dto = lecturerDtoAssembler.toDto(lecturer);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }
}
