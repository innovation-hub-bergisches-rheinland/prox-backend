package de.innovationhub.prox.modules.tag.application.tag.web;

import de.innovationhub.prox.modules.tag.application.tag.dto.MergeTagsRequest;
import de.innovationhub.prox.modules.tag.application.tag.dto.UpdateTagRequest;
import de.innovationhub.prox.modules.tag.application.tag.usecase.commands.DeleteTagHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.commands.MergeTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.commands.UpdateTagHandler;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
@Tag(name = "Tag")
@PreAuthorize("hasRole('admin')")
public class AdminTagController {
  private final MergeTagsHandler mergeTagsHandler;
  private final UpdateTagHandler updateTagHandler;
  private final DeleteTagHandler deleteTagHandler;

  @PostMapping(path = "{id}/merge", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TagDto> mergeTag(
      @PathVariable("id") UUID id,
      @Valid @RequestBody MergeTagsRequest mergeTagsRequest
  ) {
    var result = mergeTagsHandler.handle(id, mergeTagsRequest.mergeInto());
    return ResponseEntity.ok(result);
  }

  @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TagDto> updateTag(
      @PathVariable("id") UUID id,
      @Valid @RequestBody UpdateTagRequest updateTagRequest
  ) {
    var result = updateTagHandler.handle(id, updateTagRequest);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteTag(
      @PathVariable("id") UUID id
  ) {
    deleteTagHandler.handle(id);
    return ResponseEntity.noContent().build();
  }
}
