package de.innovationhub.prox.modules.project.application.project.web;

import de.innovationhub.prox.modules.project.application.project.usecase.CreateProjectHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.DeleteProjectByIdHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.FindAllProjectsHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.FindProjectByIdHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.SearchProjectHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.SetProjectPartnerHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.SetProjectTagsHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.SetSupervisorsHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.UpdateProjectHandler;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.ProjectDtoAssembler;
import de.innovationhub.prox.modules.project.application.project.web.dto.ReadProjectDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.ReadProjectListDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SetPartnerRequestDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SetProjectTagsRequestDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SetProjectTagsResponseDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.UpdateProjectDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController {

  private final FindAllProjectsHandler findAll;
  private final FindProjectByIdHandler findById;
  private final CreateProjectHandler create;
  private final UpdateProjectHandler update;
  private final SearchProjectHandler search;
  private final DeleteProjectByIdHandler deleteById;
  private final SetProjectTagsHandler setTags;
  private final SetSupervisorsHandler setSupervisors;
  private final SetProjectPartnerHandler setPartner;

  private final ProjectDtoAssembler dtoAssembler;

  @GetMapping
  public ResponseEntity<ReadProjectListDto> getAll() {
    var list = findAll.handle();
    var dtoList = dtoAssembler.toDto(list);
    return ResponseEntity.ok(dtoList);
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<ReadProjectDto> create(
      @RequestBody CreateProjectDto createDto
  ) {
    var project = create.handle(createDto);
    var dto = dtoAssembler.toDto(project);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("{id}")
  public ResponseEntity<ReadProjectDto> getById(@PathVariable("id") UUID id) {
    var projectDto = findById.handle(id)
        .map(dtoAssembler::toDto);
    if (projectDto.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(projectDto.get());
  }

  @PutMapping(value = "{id}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<ReadProjectDto> update(@PathVariable("id") UUID id, @RequestBody
  UpdateProjectDto updateProjectDto) {
    var updatedProject = update.handle(id, updateProjectDto);
    var dto = dtoAssembler.toDto(updatedProject);
    return ResponseEntity.ok(dto);
  }

  @PutMapping(value = "{id}/partner", consumes = "application/json", produces = "application/json")
  public ResponseEntity<ReadProjectDto> setPartner(@PathVariable("id") UUID id, @RequestBody
  SetPartnerRequestDto requestDto) {
    var updatedProject = setPartner.handle(id, requestDto.organizationId());
    var dto = dtoAssembler.toDto(updatedProject);
    return ResponseEntity.ok(dto);
  }

  @PostMapping(value = "{id}/supervisors", consumes = "application/json", produces = "application/json")
  public ResponseEntity<ReadProjectDto> commitment(@PathVariable("id") UUID id, @RequestBody List<UUID> supervisorIds) {
    var updatedProject = setSupervisors.handle(id, supervisorIds);
    var dto = dtoAssembler.toDto(updatedProject);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
    deleteById.handle(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("search/filter")
  public ResponseEntity<ReadProjectListDto> filter(
      @RequestParam(name = "status", required = false) ProjectState status,
      @RequestParam(name = "disciplineKeys", required = false) Collection<String> specializationKeys,
      @RequestParam(name = "moduleTypeKeys", required = false) Collection<String> moduleTypeKeys,
      @RequestParam(name = "text", required = false) String text) {
    var result = search.handle(status, specializationKeys, moduleTypeKeys, text);
    var dtoList = dtoAssembler.toDto(result);
    return ResponseEntity.ok(dtoList);
  }

  @PostMapping(value = "{id}/tags", consumes = "application/json", produces = "application/json")
  public ResponseEntity<SetProjectTagsResponseDto> setTags(
      @PathVariable("id") UUID id,
      @RequestBody SetProjectTagsRequestDto tagsDto) {
    var result = setTags.handle(id, tagsDto.tags());
    return ResponseEntity.ok(new SetProjectTagsResponseDto(result));
  }
}
