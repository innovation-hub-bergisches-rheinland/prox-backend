package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "MembershipsResponse")
public record MembershipsResponseDto(
    @JsonProperty("members") List<MembershipDto> members) {
}
