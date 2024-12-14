package br.com.soupaulodev.forumhub.modules.topic.usecase;

import br.com.soupaulodev.forumhub.modules.exception.usecase.ForumNotFoundException;
import br.com.soupaulodev.forumhub.modules.exception.usecase.UserNotFoundException;
import br.com.soupaulodev.forumhub.modules.forum.entity.ForumEntity;
import br.com.soupaulodev.forumhub.modules.forum.repository.ForumRepository;
import br.com.soupaulodev.forumhub.modules.topic.controller.dto.TopicCreateRequestDTO;
import br.com.soupaulodev.forumhub.modules.topic.controller.dto.TopicResponseDTO;
import br.com.soupaulodev.forumhub.modules.topic.entity.TopicEntity;
import br.com.soupaulodev.forumhub.modules.topic.mapper.TopicMapper;
import br.com.soupaulodev.forumhub.modules.topic.repository.TopicRepository;
import br.com.soupaulodev.forumhub.modules.user.entity.UserEntity;
import br.com.soupaulodev.forumhub.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for creating a new topic.
 */
@Service
public class CreateTopicUseCase {

    private final TopicRepository topicRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new CreateTopicUsecase with the specified repositories.
     *
     * @param topicRepository the repository for managing topics
     * @param forumRepository the repository for managing forums
     * @param userRepository the repository for managing users
     */
    public CreateTopicUseCase(TopicRepository topicRepository,
                              ForumRepository forumRepository,
                              UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
    }

    /**
     * Executes the use case to create a new topic.
     *
     * @param requestDTO the data transfer object containing the topic creation data
     * @return the response data transfer object containing the created topic data
     * @throws ForumNotFoundException if the forum specified in the request does not exist
     * @throws UserNotFoundException if the user specified in the request does not exist
     */
    public TopicResponseDTO execute(TopicCreateRequestDTO requestDTO) {

        ForumEntity forum = forumRepository.findById(UUID.fromString(requestDTO.forumId()))
                .orElseThrow(ForumNotFoundException::new);
        UserEntity user = userRepository.findById(UUID.fromString(requestDTO.creatorId()))
                .orElseThrow(UserNotFoundException::new);

        TopicEntity topic = TopicMapper.toEntity(requestDTO, forum, user);
        topic.setCreator(user);
        topic.setForum(forum);
        forum.addTopics(topic);
        user.addTopic(topic);

        TopicEntity topicSaved = topicRepository.save(topic);
        return TopicMapper.toResponseDTO(topicSaved);
    }
}