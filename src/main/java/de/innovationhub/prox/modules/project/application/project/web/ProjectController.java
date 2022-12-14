package de.innovationhub.prox.modules.project.application.project.web;

import de.innovationhub.prox.modules.project.application.project.usecase.commands.CreateProjectHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.DeleteProjectByIdHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.SetProjectPartnerHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.SetProjectTagsHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.SetStateHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.SetSupervisorsHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.UpdateProjectHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindAllProjectsHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindProjectByIdHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindProjectsOfPartnerHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.FindProjectsOfSupervisorHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.queries.SearchProjectHandler;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectRequest;
import de.innovationhub.prox.modules.project.application.project.web.dto.ProjectDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.ProjectDtoAssembler;
import de.innovationhub.prox.modules.project.application.project.web.dto.ReadProjectListDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SetPartnerRequestDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SetProjectStateRequestDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SetProjectTagsRequestDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Project", description = "Project API")
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
  private final FindProjectsOfPartnerHandler findByPartner;
  private final FindProjectsOfSupervisorHandler findBySupervisor;
  private final SetStateHandler setState;

  private final ProjectDtoAssembler dtoAssembler;

  @GetMapping
  public ResponseEntity<ReadProjectListDto> getAll() {
    var list = findAll.handle();
    var dtoList = dtoAssembler.toDto(list);
    return ResponseEntity.ok(dtoList);
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<ProjectDto> create(
      @RequestBody CreateProjectRequest createDto
  ) {
    var project = create.handle(createDto);
    var dto = dtoAssembler.toDto(project);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("{id}")
  public ResponseEntity<ProjectDto> getById(@PathVariable("id") UUID id) {
    var projectDto = findById.handle(id)
        .map(dtoAssembler::toDto);
    if (projectDto.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(projectDto.get());
  }

  @PutMapping(value = "{id}", consumes = "application/json", produces = "application/json")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<ProjectDto> update(@PathVariable("id") UUID id, @RequestBody
  CreateProjectRequest updateProjectDto) {
    var updatedProject = update.handle(id, updateProjectDto);
    var dto = dtoAssembler.toDto(updatedProject);
    return ResponseEntity.ok(dto);
  }

  @PostMapping(value = "{id}/status", consumes = "application/json", produces = "application/json")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<ProjectDto> setState(@PathVariable("id") UUID id, @RequestBody
  SetProjectStateRequestDto requestDto) {
    var updatedProject = setState.handle(id, requestDto.state());
    var dto = dtoAssembler.toDto(updatedProject);
    return ResponseEntity.ok(dto);
  }

  @PostMapping(value = "{id}/partner", consumes = "application/json", produces = "application/json")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<ProjectDto> setPartner(@PathVariable("id") UUID id, @RequestBody
  SetPartnerRequestDto requestDto) {
    var updatedProject = setPartner.handle(id, requestDto.organizationId());
    var dto = dtoAssembler.toDto(updatedProject);
    return ResponseEntity.ok(dto);
  }

  @PostMapping(value = "{id}/supervisors", consumes = "application/json", produces = "application/json")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  @PreAuthorize("@projectPermissionEvaluator.hasPermission(#projectId, authentication)")
  public ResponseEntity<ProjectDto> commitment(@PathVariable("id") UUID id, @RequestBody List<UUID> supervisorIds) {
    var updatedProject = setSupervisors.handle(id, supervisorIds);
    var dto = dtoAssembler.toDto(updatedProject);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("{id}")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
    deleteById.handle(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("search/filter")
  public ResponseEntity<ReadProjectListDto> filter(
      @RequestParam(name = "status", required = false) ProjectState status,
      @RequestParam(name = "disciplineKeys", required = false, defaultValue = "") Collection<String> specializationKeys,
      @RequestParam(name = "moduleTypeKeys", required = false, defaultValue = "") Collection<String> moduleTypeKeys,
      @RequestParam(name = "text", required = false, defaultValue = "") String text) {
    var result = search.handle(status, specializationKeys, moduleTypeKeys, text);
    var dtoList = dtoAssembler.toDto(result);
    return ResponseEntity.ok(dtoList);
  }

  @GetMapping("search/findBySupervisor")
  public ResponseEntity<ReadProjectListDto> findBySupervisor(
      @RequestParam(name = "supervisor") UUID supervisorId) {
    var result = findBySupervisor.handle(supervisorId);
    var dtoList = dtoAssembler.toDto(result);
    return ResponseEntity.ok(dtoList);
  }

  @GetMapping("search/findByPartner")
  public ResponseEntity<ReadProjectListDto> findByPartner(
      @RequestParam(name = "partner") UUID partner) {
    var result = findByPartner.handle(partner);
    var dtoList = dtoAssembler.toDto(result);
    return ResponseEntity.ok(dtoList);
  }

  @PostMapping(value = "{id}/tags", consumes = "application/json", produces = "application/json")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<ProjectDto> setTags(
      @PathVariable("id") UUID id,
      @RequestBody SetProjectTagsRequestDto tagsDto) {
    var result = setTags.handle(id, tagsDto.tags());
    var dto = dtoAssembler.toDto(result);
    return ResponseEntity.ok(dto);
  }
}
