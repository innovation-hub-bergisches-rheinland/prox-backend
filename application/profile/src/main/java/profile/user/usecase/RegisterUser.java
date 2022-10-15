package profile.user.usecase;

import commons.usecase.UseCase;
import java.util.UUID;

public record RegisterUser(UUID id, String name, String email) implements UseCase {

}
