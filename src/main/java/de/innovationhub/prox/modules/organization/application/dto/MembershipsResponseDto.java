package de.innovationhub.prox.modules.organization.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.modules.organization.contract.dto.MembershipDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "MembershipsResponse")
public record MembershipsResponseDto(
    @JsonProperty("members") List<MembershipDto> members) {
}
