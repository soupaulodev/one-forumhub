package br.com.soupaulodev.forumhub.modules.like.usecase;

import br.com.soupaulodev.forumhub.modules.exception.usecase.ResourceNotFoundException;
import br.com.soupaulodev.forumhub.modules.like.controller.dto.LikeRequestDTO;
import br.com.soupaulodev.forumhub.modules.like.entity.LikeEntity;
import br.com.soupaulodev.forumhub.modules.like.repository.LikeRepository;
import br.com.soupaulodev.forumhub.modules.like.usecase.strategy.LikeResourceStrategy;
import br.com.soupaulodev.forumhub.modules.like.usecase.strategy.LikeResourceStrategyManager;
import br.com.soupaulodev.forumhub.modules.user.entity.UserEntity;
import br.com.soupaulodev.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for dislike a resource.
 * This class provides the business logic for unliking a resource.
 *
 * @author <a href="https://soupaulodev.com.br">soupaulodev</a>
 */
@Service
public class DislikeResourceUsecase {

    private final LikeResourceStrategyManager strategyManager;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    /**
     * Construct's a new {@link DislikeResourceUsecase}.
     *
     * @param strategyManager the like resource strategy manager
     * @param userRepository   the user repository
     */
    public DislikeResourceUsecase(LikeResourceStrategyManager strategyManager, UserRepository userRepository, LikeRepository likeRepository) {
        this.strategyManager = strategyManager;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    /**
     * Executes the use case.
     *
     * @param requestDTO         the request DTO
     * @param authenticatedUserId the authenticated user id
     */
    public void execute(LikeRequestDTO requestDTO, UUID authenticatedUserId) {
        UserEntity user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        LikeResourceStrategy strategy = strategyManager.getStrategy(requestDTO.resourceType());
        LikeEntity like = likeRepository.findByResourceTypeAndResourceIdAndUser(
                        requestDTO.resourceType(),
                        UUID.fromString(requestDTO.resourceId()),
                        user)
                .orElseThrow(() -> new ResourceNotFoundException("Like does not exist"));

        strategy.dislikeResource(UUID.fromString(requestDTO.resourceId()));
        likeRepository.delete(like);
    }
}