package de.innovationhub.prox.modules.tag.application.tag.web;

import de.innovationhub.prox.modules.tag.application.tag.dto.MergeTagsRequest;
import de.innovationhub.prox.modules.tag.application.tag.dto.SynchronizeTagsRequest;
import de.innovationhub.prox.modules.tag.application.tag.dto.SynchronizeTagsResponse;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.application.tag.dto.UpdateTagAliasesRequest;
import de.innovationhub.prox.modules.tag.application.tag.usecase.commands.MergeTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.commands.SynchronizeTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.commands.UpdateTagAliasesHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindCommonTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindMatchingTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindPopularTagsHandler;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
@Tag(name = "Tag")
@PreAuthorize("hasRole('admin')")
public class AdminTagController {
  private final MergeTagsHandler mergeTagsHandler;
  private final UpdateTagAliasesHandler updateTagAliasesHandler;

  @PostMapping(path = "{id}/merge", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TagDto> mergeTag(
      @PathVariable("id") UUID id,
      @Valid @RequestBody MergeTagsRequest mergeTagsRequest
  ) {
    var result = mergeTagsHandler.handle(id, mergeTagsRequest.mergeInto());
    return ResponseEntity.ok(result);
  }

  @PutMapping(path = "{id}/aliases", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Retrieve recommendation for provided Tags")
  public ResponseEntity<TagDto> findRecommendations(
      @PathVariable("id") UUID id,
      @Valid @RequestBody UpdateTagAliasesRequest updateTagAliasesRequest
  ) {
    var result = updateTagAliasesHandler.handle(id, updateTagAliasesRequest.aliases());
    return ResponseEntity.ok(result);
  }
}
