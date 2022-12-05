package de.innovationhub.prox.modules.project.application.module.web;

import de.innovationhub.prox.modules.project.application.module.usecase.FindAllModulesHandler;
import de.innovationhub.prox.modules.project.application.module.usecase.FindModulesByDisciplinesHandler;
import de.innovationhub.prox.modules.project.application.module.web.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.application.module.web.dto.ReadModuleTypeDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("modules")
@Tag(name = "Curriculum")
public class ModuleController {

  private final FindAllModulesHandler findAllModulesHandler;
  private final FindModulesByDisciplinesHandler findModulesByDisciplines;
  private final ModuleTypeMapper moduleTypeMapper;

  public ModuleController(FindAllModulesHandler findAllModulesHandler,
      FindModulesByDisciplinesHandler findModulesByDisciplines, ModuleTypeMapper moduleTypeMapper) {
    this.findAllModulesHandler = findAllModulesHandler;
    this.findModulesByDisciplines = findModulesByDisciplines;
    this.moduleTypeMapper = moduleTypeMapper;
  }

  @GetMapping
  public ResponseEntity<Page<ReadModuleTypeDto>> getAll(
      @ParameterObject @PageableDefault(size = 100) Pageable pageable
  ) {
    return ResponseEntity.ok(
        moduleTypeMapper.toDtoPage(
            findAllModulesHandler.handle(pageable)
        )
    );
  }

  @GetMapping("search/findByDisciplines")
  public ResponseEntity<Page<ReadModuleTypeDto>> findByDisciplines(
      @RequestParam("keys") List<String> disciplineKeys,
      @ParameterObject @PageableDefault(size = 100) Pageable pageable) {
    return ResponseEntity.ok(
        moduleTypeMapper.toDtoPage(
            findModulesByDisciplines.handle(disciplineKeys, pageable)
        )
    );
  }
}
