package de.innovationhub.prox.modules.project.application.project.web;

import de.innovationhub.prox.modules.project.application.project.usecase.DeleteProjectByIdHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.FindAllProjectsHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.FindProjectByIdHandler;
import de.innovationhub.prox.modules.project.application.project.usecase.SearchProjectHandler;
import de.innovationhub.prox.modules.project.application.project.web.dto.ProjectDtoAssembler;
import de.innovationhub.prox.modules.project.application.project.web.dto.ReadProjectDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.ReadProjectListDto;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController {
  private final FindAllProjectsHandler findAll;
  private final FindProjectByIdHandler findById;
  private final SearchProjectHandler search;
  private final DeleteProjectByIdHandler deleteById;

  private final ProjectDtoAssembler dtoAssembler;

  @GetMapping
  public ResponseEntity<ReadProjectListDto> getAll() {
    var list = findAll.handle();
    var dtoList = dtoAssembler.toDto(list);
    return ResponseEntity.ok(dtoList);
  }

  @GetMapping("{id}")
  public ResponseEntity<ReadProjectDto> getById(@PathVariable("id") UUID id) {
    var projectDto = findById.handle(id)
        .map(dtoAssembler::toDto);
    if(projectDto.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(projectDto.get());
  }

  @GetMapping("search/filter")
  public ResponseEntity<ReadProjectListDto> filter(
      @RequestParam(name = "status", required = false) ProjectState status,
      @RequestParam(name = "specializationKeys", required = false) Collection<String> specializationKeys,
      @RequestParam(name = "moduleTypeKeys", required = false) Collection<String> moduleTypeKeys,
      @RequestParam(name = "text", required = false) String text) {
    var result = search.handle(status, specializationKeys, moduleTypeKeys, text);
    var dtoList = dtoAssembler.toDto(result);
    return ResponseEntity.ok(dtoList);
  }
}
