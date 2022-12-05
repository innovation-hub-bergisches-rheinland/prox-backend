package de.innovationhub.prox.modules.profile.application.organization.web;

import de.innovationhub.prox.modules.profile.application.organization.usecase.commands.AddOrganizationMemberHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.commands.CreateOrganizationHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.queries.FindAllOrganizationsHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.queries.FindOrganizationHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.queries.FindOrganizationMembershipsHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.commands.RemoveOrganizationMemberHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.commands.SetOrganizationLogoHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.commands.SetOrganizationTagsHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.commands.UpdateOrganizationHandler;
import de.innovationhub.prox.modules.profile.application.organization.usecase.commands.UpdateOrganizationMemberHandler;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.AddOrganizationMembershipDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.CreateOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.OrganizationDtoAssembler;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.ReadOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.ReadOrganizationMembershipDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.SetOrganizationTagsRequestDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.SetOrganizationTagsResponseDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.UpdateOrganizationDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.UpdateOrganizationMembershipDto;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.ViewAllOrganizationMembershipsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("organizations")
@RequiredArgsConstructor
@Tag(name = "Organization", description = "Organization API")
public class OrganizationController {

  private final CreateOrganizationHandler create;
  private final FindOrganizationHandler find;
  private final FindAllOrganizationsHandler findAll;
  private final UpdateOrganizationHandler update;
  private final AddOrganizationMemberHandler addMember;
  private final FindOrganizationMembershipsHandler findMember;
  private final UpdateOrganizationMemberHandler updateMember;
  private final RemoveOrganizationMemberHandler removeMember;
  private final SetOrganizationLogoHandler setLogo;
  private final SetOrganizationTagsHandler setTags;

  private final OrganizationDtoAssembler dtoAssembler;

  @GetMapping
  public ResponseEntity<List<ReadOrganizationDto>> getAll() {
    var dtoList = findAll.handle()
        .stream().map(dtoAssembler::toDto)
        .toList();
    return ResponseEntity.ok(dtoList);
  }

  @PostMapping
  public ResponseEntity<ReadOrganizationDto> create(
      @RequestBody CreateOrganizationDto createOrganizationDto
  ) {
    var org = create.handle(createOrganizationDto);
    var dto = dtoAssembler.toDto(org);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("{id}")
  public ResponseEntity<ReadOrganizationDto> get(
      @PathVariable("id") UUID id
  ) {
    var dto = find.handle(id)
        .map(dtoAssembler::toDto);
    if (dto.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(dto.get());
  }

  @PutMapping("{id}")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<ReadOrganizationDto> update(
      @PathVariable("id") UUID id,
      @RequestBody UpdateOrganizationDto updateOrganizationDto
  ) {
    var org = update.handle(id, updateOrganizationDto);
    var dto = dtoAssembler.toDto(org);
    return ResponseEntity.ok(dto);
  }

  @GetMapping("{id}/memberships")
  public ResponseEntity<ViewAllOrganizationMembershipsDto> getAllMemberships(
      @PathVariable("id") UUID id
  ) {
    var memberships = findMember.handle(id);
    var dto = dtoAssembler.toDto(memberships);
    return ResponseEntity.ok(new ViewAllOrganizationMembershipsDto(dto));
  }

  @PostMapping("{id}/memberships")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<ReadOrganizationMembershipDto> addMembership(
      @PathVariable("id") UUID id,
      @RequestBody AddOrganizationMembershipDto addMemberDto
  ) {
    var membership = addMember.handle(id, addMemberDto);
    var dto = dtoAssembler.toDto(membership);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @PutMapping("{id}/memberships/{memberId}")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<ReadOrganizationMembershipDto> updateMembership(
      @PathVariable("id") UUID id,
      @PathVariable("memberId") UUID memberId,
      @RequestBody UpdateOrganizationMembershipDto updateDto
  ) {
    var membership = updateMember.handle(id, memberId, updateDto);
    var dto = dtoAssembler.toDto(membership);
    return ResponseEntity.ok(dto);
  }

  @DeleteMapping("{id}/memberships/{memberId}")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<Void> removeMembership(
      @PathVariable("id") UUID id,
      @PathVariable("memberId") UUID memberId
  ) {
    removeMember.handle(id, memberId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping(value = "{id}/logo", consumes = "multipart/form-data")
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
    setLogo.handle(id, multipartFile.getBytes(), contentType);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/tags")
  @Operation(security = {
      @SecurityRequirement(name = "oidc")
  })
  public ResponseEntity<SetOrganizationTagsResponseDto> setTags(
      @PathVariable("id") UUID id,
      @RequestBody SetOrganizationTagsRequestDto tagsDto) {
    var result = setTags.handle(id, tagsDto.tags());
    return ResponseEntity.ok(new SetOrganizationTagsResponseDto(result));
  }
}
