package de.innovationhub.prox.modules.tag.application.tag.web;

import de.innovationhub.prox.modules.tag.application.tag.dto.TagDto;
import de.innovationhub.prox.modules.tag.application.tag.usecase.FindMatchingTags;
import de.innovationhub.prox.modules.tag.application.tag.usecase.FindMatchingTagsHandler;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagController {
  private final FindMatchingTagsHandler findMatchingTagsHandler;

  public TagController(FindMatchingTagsHandler findMatchingTagsHandler) {
    this.findMatchingTagsHandler = findMatchingTagsHandler;
  }

  @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<TagDto>> findMatchingTags(@RequestParam("q") String partialTag) {
    return ResponseEntity.ok(findMatchingTagsHandler.handle(new FindMatchingTags(partialTag)));
  }
}
