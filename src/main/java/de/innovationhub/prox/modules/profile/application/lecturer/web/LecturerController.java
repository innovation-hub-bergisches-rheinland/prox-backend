package de.innovationhub.prox.modules.profile.application.lecturer.web;

import de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands.CreateLecturerHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.queries.FilterLecturerHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.queries.FindAllLecturersHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.queries.FindLecturerHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands.SetLecturerAvatarHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands.SetLecturerTagsHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands.UpdateLecturerProfileHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.LecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.LecturerDtoAssembler;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.SetLecturerTagsRequestDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.SetLecturerTagsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("lecturers")
@RequiredArgsConstructor
@Tag(name = "Lecturer", description = "Lecturer API")
public class LecturerController {
  private final CreateLecturerHandler create;
  private final FindAllLecturersHandler findAll;
  private final FindLecturerHandler find;
  private final UpdateLecturerProfileHandler update;
  private final SetLecturerAvatarHandler setAvatar;
  private final SetLecturerTagsHandler setTags;
  private final FilterLecturerHandler filter;
  private final LecturerDtoAssembler dtoAssembler;

  @GetMapping
  public ResponseEntity<List<LecturerDto>> getAll() {
    var dtoList = findAll.handle().stream()
        .map(dtoAssembler::toDto)
        .toList();
    return ResponseEntity.ok(dtoList);
  }

  @GetMapping("{id}")
  public ResponseEntity<LecturerDto> getById(@PathVariable("id") UUID id) {
    var dto = find.handle(id)
        .map(dtoAssembler::toDto);
    if(dto.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(dto.get());
  }

  @PostMapping
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<LecturerDto> create(
      @RequestBody CreateLecturerRequestDto createLecturerRequestDto
  ) {
    var lecturer = create.handle(createLecturerRequestDto);
    var dto = dtoAssembler.toDto(lecturer);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @PutMapping("{id}")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<LecturerDto> update(
      @PathVariable("id") UUID id,
      @RequestBody CreateLecturerRequestDto updateLecturerDto
  ) {
    var lecturer = update.handle(id, updateLecturerDto);
    var dto = dtoAssembler.toDto(lecturer);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @PostMapping(value = "{id}/avatar", consumes = "multipart/form-data")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<Void> postAvatar(
      @PathVariable("id") UUID id,
      @RequestParam("image") MultipartFile multipartFile
  ) throws IOException {
    if(multipartFile.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    var contentType = multipartFile.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
    }
    setAvatar.handle(id, multipartFile.getBytes(), contentType);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/tags")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<SetLecturerTagsResponseDto> setTags(
      @PathVariable("id") UUID id,
      @RequestBody SetLecturerTagsRequestDto tagsDto) {
    var result = setTags.handle(id, tagsDto.tags());
    return ResponseEntity.ok(new SetLecturerTagsResponseDto(result));
  }

  @GetMapping("search/filter")
  public ResponseEntity<List<LecturerDto>> getById(@RequestParam("q") String query) {
    var dto = filter.handle(query).stream()
        .map(dtoAssembler::toDto)
        .toList();

    return ResponseEntity.ok(dto);
  }
}
