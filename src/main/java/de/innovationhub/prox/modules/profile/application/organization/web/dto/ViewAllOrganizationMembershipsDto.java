package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ViewAllOrganizationMembershipsDto(
    @JsonProperty("members") List<ReadOrganizationMembershipDto> members) {
}
