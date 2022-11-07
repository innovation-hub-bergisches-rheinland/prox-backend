package de.innovationhub.prox.modules.tag.application.tag.web;

import de.innovationhub.prox.modules.tag.application.tag.usecase.FindCommonTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.FindMatchingTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.usecase.FindPopularTagsHandler;
import de.innovationhub.prox.modules.tag.application.tag.web.dto.TagDto;
import de.innovationhub.prox.modules.tag.application.tag.web.dto.TagDtoMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
public class TagController {

  private final FindMatchingTagsHandler findMatchingTagsHandler;
  private final FindCommonTagsHandler findCommonTagsHandler;
  private final FindPopularTagsHandler findPopularTagsHandler;
  private final TagDtoMapper tagDtoMapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<TagDto>> findMatchingTags(@RequestParam("q") String partialTag) {
    var result = findMatchingTagsHandler.handle(partialTag);
    var dto = tagDtoMapper.toDtoList(result);

    return ResponseEntity.ok(dto);
  }

  @GetMapping(path = "recommendations", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<TagDto>> findRecommendations(
      @RequestParam("tags") List<String> inputTags,
      @RequestParam(value = "limit", defaultValue = "10") int limit) {
    var result = findCommonTagsHandler.handle(inputTags, limit);
    var dto = tagDtoMapper.toDtoList(result);

    return ResponseEntity.ok(dto);
  }

  @GetMapping(path = "popular", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<TagDto>> findPopular(
      @RequestParam(value = "limit", defaultValue = "10") int limit) {
    var result = findPopularTagsHandler.handle(limit);
    var dto = tagDtoMapper.toDtoList(result);

    return ResponseEntity.ok(dto);
  }
}
