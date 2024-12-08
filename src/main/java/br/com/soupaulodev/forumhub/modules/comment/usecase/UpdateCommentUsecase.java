package br.com.soupaulodev.forumhub.modules.comment.usecase;

import br.com.soupaulodev.forumhub.modules.comment.controller.dto.CommentResponseDTO;
import br.com.soupaulodev.forumhub.modules.comment.controller.dto.CommentUpdateRequestDTO;
import br.com.soupaulodev.forumhub.modules.comment.entity.CommentEntity;
import br.com.soupaulodev.forumhub.modules.comment.mapper.CommentMapper;
import br.com.soupaulodev.forumhub.modules.comment.repository.CommentRepository;
import br.com.soupaulodev.forumhub.modules.exception.usecase.CommentIllegalArgumentException;
import br.com.soupaulodev.forumhub.modules.exception.usecase.CommentNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Service class for updating comments.
 */
@Service
public class UpdateCommentUsecase {

    private final CommentRepository commentRepository;

    /**
     * Constructs a new UpdateCommentUsecase with the specified repository.
     *
     * @param commentRepository the repository for managing comments
     */
    public UpdateCommentUsecase(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Executes the use case to update a comment.
     *
     * @param id the UUID of the comment to be updated
     * @param requestDTO the DTO containing the updated comment data
     * @return the response DTO with the updated comment data
     * @throws CommentNotFoundException if the comment is not found
     * @throws CommentIllegalArgumentException if the new content is the same as the old content or if the content is empty
     */
    public CommentResponseDTO execute(UUID id, CommentUpdateRequestDTO requestDTO) {

        CommentEntity commentFound = commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);

        if (requestDTO.getContent().equals(commentFound.getContent())) {
            throw new CommentIllegalArgumentException();
        }

        if (requestDTO.getContent().isEmpty()) {
            throw new CommentIllegalArgumentException("Comment content cannot be empty");
        }

        commentFound.setContent(requestDTO.getContent());
        commentFound.setUpdatedAt(Instant.now());

        CommentEntity commentUpdated = commentRepository.save(commentFound);
        return CommentMapper.toResponseDTO(commentUpdated);
    }
}