package de.innovationhub.prox.modules.project.application.module.web;

import de.innovationhub.prox.modules.project.application.module.usecase.queries.FindAllModulesHandler;
import de.innovationhub.prox.modules.project.application.module.usecase.queries.FindModulesByDisciplinesHandler;
import de.innovationhub.prox.modules.project.application.module.web.dto.ModuleTypeMapper;
import de.innovationhub.prox.modules.project.application.module.web.dto.ReadModuleTypeDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
  public ResponseEntity<List<ReadModuleTypeDto>> getAll() {
    return ResponseEntity.ok(
        moduleTypeMapper.toDtoList(
            findAllModulesHandler.handle()
        )
    );
  }

  @GetMapping("search/findByDisciplines")
  public ResponseEntity<List<ReadModuleTypeDto>> findByDisciplines(
      @RequestParam("keys") List<String> disciplineKeys) {
    return ResponseEntity.ok(
        moduleTypeMapper.toDtoList(
            findModulesByDisciplines.handle(disciplineKeys)
        )
    );
  }
}
