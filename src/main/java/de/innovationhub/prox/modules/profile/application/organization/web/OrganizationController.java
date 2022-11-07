package de.innovationhub.prox.modules.profile.application.organization.web;

import de.innovationhub.prox.modules.profile.application.organization.web.dto.OrganizationDtoAssembler;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.ReadOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.CreateOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.usecase.CreateOrganizationHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.FindAllOrganizationsHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.FindOrganizationHandler;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.UpdateOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.usecase.UpdateOrganizationHandler;
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
@RequestMapping("organizations")
@RequiredArgsConstructor
public class OrganizationController {
  private final CreateOrganizationHandler createOrganizationHandler;
  private final FindOrganizationHandler findOrganizationHandler;
  private final FindAllOrganizationsHandler findAllOrganizationsHandler;
  private final UpdateOrganizationHandler updateOrganizationHandler;
  private final OrganizationDtoAssembler organizationDtoAssembler;

  @GetMapping
  public ResponseEntity<List<ReadOrganizationDto>> getAll() {
    var dtoList = findAllOrganizationsHandler.handle()
        .stream().map(organizationDtoAssembler::toDto)
        .toList();
    return ResponseEntity.ok(dtoList);
  }

  @PostMapping
  public ResponseEntity<ReadOrganizationDto> create(
      @RequestBody CreateOrganizationDto createOrganizationDto
  ) {
    var org = createOrganizationHandler.handle(createOrganizationDto);
    var dto = organizationDtoAssembler.toDto(org);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("{id}")
  public ResponseEntity<ReadOrganizationDto> get(
      @PathVariable("id") UUID id
  ) {
    var dto = findOrganizationHandler.handle(id)
        .map(organizationDtoAssembler::toDto);
    if(dto.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(dto.get());
  }

  @PutMapping("{id}")
  public ResponseEntity<ReadOrganizationDto> update(
      @PathVariable("id") UUID id,
      @RequestBody UpdateOrganizationDto updateOrganizationDto
  ) {
    var org = updateOrganizationHandler.handle(id, updateOrganizationDto);
    var dto = organizationDtoAssembler.toDto(org);
    return ResponseEntity.ok(dto);
  }
}
