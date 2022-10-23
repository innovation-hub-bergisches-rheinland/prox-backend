package de.innovationhub.prox.modules.profile.domain.organization;

import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
  @Id
  private UUID id = UUID.randomUUID();

  @Embedded
  @NotNull
  private UserAccount user;

  public Member(UserAccount user) {
    Objects.requireNonNull(user);
    this.user = user;
  }
}
