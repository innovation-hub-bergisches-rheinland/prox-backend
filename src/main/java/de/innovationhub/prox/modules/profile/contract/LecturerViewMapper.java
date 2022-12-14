package de.innovationhub.prox.modules.profile.contract;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface LecturerViewMapper {
  LecturerView toView(Lecturer lecturer);
  List<LecturerView> toViewList(List<Lecturer> lecturers);
}
