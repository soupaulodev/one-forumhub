package br.com.soupaulodev.forumhub.modules.user.usecase;

import br.com.soupaulodev.forumhub.modules.user.repository.UserHighsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UnHighUserUseCase {

    private final UserHighsRepository userHighsRepository;

    public UnHighUserUseCase(UserHighsRepository userHighsRepository) {
        this.userHighsRepository = userHighsRepository;
    }

    public void execute(UUID highedUser, UUID authenticatedUserId) {
        if (highedUser.equals(authenticatedUserId)) {
            throw new IllegalArgumentException("User cannot unhigh himself");
        }

        userHighsRepository.findByHighedUser_IdAndHighingUser_Id(highedUser, authenticatedUserId)
                .ifPresentOrElse(userHighsRepository::delete, () -> {
                    throw new IllegalArgumentException("User not highed");
                });
    }
}