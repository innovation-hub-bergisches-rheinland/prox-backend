package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoMapper;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerTagsDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerPort;
import de.innovationhub.prox.modules.tag.application.tag.dto.TagDto;
import de.innovationhub.prox.modules.tag.application.tag.usecase.FindOrCreateTag;
import de.innovationhub.prox.modules.tag.application.tag.usecase.FindOrCreateTagHandler;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class TagLecturerHandler implements UseCaseHandler<LecturerTagsDto, TagLecturer> {
  private final FindOrCreateTagHandler findOrCreateTagHandler;
  private final LecturerPort lecturerPort;
  private final LecturerDtoMapper lecturerDtoMapper;
  @Override
  public LecturerTagsDto handle(TagLecturer useCase) {
    var lecturer = lecturerPort.getById(useCase.lecturerId())
        .orElseThrow(() -> new RuntimeException("Lecturer does not exist"));

    var tags = useCase.tags()
        .stream()
        .map(t -> findOrCreateTagHandler.handle(new FindOrCreateTag(t)))
        .toList();

    var tagIds = tags.stream()
        .map(TagDto::id)
        .toList();

    lecturer.setTags(tagIds);
    lecturerPort.save(lecturer);

    return lecturerDtoMapper.toTagsDto(tags.stream().map(TagDto::tag).toList());
  }
}
