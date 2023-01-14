package de.innovationhub.prox.modules.user.application.account.usecase.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;

class AssignProfessorGroupTest {

  RealmResource realmResource = mock(RealmResource.class, RETURNS_DEEP_STUBS);

  AssignProfessorGroup assignProfessorGroup = new AssignProfessorGroup(realmResource);

  @Test
  void shouldThrowOnNull() {
    assertThatThrownBy(() -> assignProfessorGroup.handle(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldThrowWhenNoGroupIsFound() {
    when(realmResource.groups()
        .groups(anyString(), anyBoolean(), anyInt(), anyInt(), anyBoolean())).thenReturn(List.of());
    assertThatThrownBy(() -> assignProfessorGroup.handle("userId"))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldThrowWhenMultipleGroupsFound() {
    when(realmResource.groups()
        .groups(anyString(), anyBoolean(), anyInt(), anyInt(), anyBoolean())).thenReturn(
        List.of(mock(GroupRepresentation.class), mock(
            GroupRepresentation.class)));
    assertThatThrownBy(() -> assignProfessorGroup.handle("userId"))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldAssignGroup() {
    var group = mock(GroupRepresentation.class);
    when(group.getId()).thenReturn("groupId");
    when(realmResource.groups()
        .groups(anyString(), anyBoolean(), anyInt(), anyInt(), anyBoolean())).thenReturn(
        List.of(group));
    assignProfessorGroup.handle("userId");

    verify(realmResource.users().get("userId")).joinGroup("groupId");
  }
}