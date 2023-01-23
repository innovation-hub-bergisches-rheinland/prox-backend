package de.innovationhub.prox.modules.tag.application.tag.web;

import de.innovationhub.prox.modules.tag.application.tag.dto.SynchronizeTagsRequest;
import de.innovationhub.prox.modules.tag.application.tag.dto.SynchronizeTagsResponse;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDto;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDtoMapper;
import de.innovationhub.prox.modules.tag.application.tag.usecase.commands.SynchronizeTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindCommonTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindMatchingTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.queries.FindPopularTagsHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
@Tag(name = "Tag", description = "Tag API")
public class TagController {

  private final FindMatchingTagsHandler findMatching;
  private final FindCommonTagsHandler findCommon;
  private final FindPopularTagsHandler findPopular;
  private final SynchronizeTagsHandler synchronize;
  private final TagDtoMapper dtoMapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Find tags that match a given input")
  public ResponseEntity<List<TagDto>> findMatchingTags(@RequestParam("q") String partialTag) {
    var result = findMatching.handle(partialTag);
    var dto = dtoMapper.toDtoList(result);

    return ResponseEntity.ok(dto);
  }

  @GetMapping(path = "recommendations", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Retrieve recommendation for provided Tags")
  public ResponseEntity<List<TagDto>> findRecommendations(
      @RequestParam("tags") List<String> inputTags,
      @RequestParam(value = "limit", defaultValue = "10") int limit) {
    var result = findCommon.handle(inputTags, limit);
    var dto = dtoMapper.toDtoList(result);

    return ResponseEntity.ok(dto);
  }

  @GetMapping(path = "popular", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Get the most popular tags")
  public ResponseEntity<List<TagDto>> findPopular(
      @RequestParam(value = "limit", defaultValue = "10") int limit) {
    var result = findPopular.handle(limit);
    var dto = dtoMapper.toDtoList(result);

    return ResponseEntity.ok(dto);
  }

  @PostMapping(path = "synchronization", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      description = "Synchronizes provided tag names with tag entities from remote.",
      security = {
          @SecurityRequirement(name = "oidc")
      })
  public ResponseEntity<SynchronizeTagsResponse> synchronizeTags(
      @RequestBody SynchronizeTagsRequest request) {
    var result = synchronize.handle(request.tags());

    return ResponseEntity.ok(dtoMapper.toSynchronizeTagsResponse(result));
  }
}
