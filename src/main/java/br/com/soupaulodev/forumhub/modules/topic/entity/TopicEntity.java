package br.com.soupaulodev.forumhub.modules.topic.entity;

import br.com.soupaulodev.forumhub.modules.comment.entity.CommentEntity;
import br.com.soupaulodev.forumhub.modules.forum.entity.ForumEntity;
import br.com.soupaulodev.forumhub.modules.like.entity.LikeEntity;
import br.com.soupaulodev.forumhub.modules.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

/**
 * Entity representing a topic in the forum.
 */
@Entity
@Table(name = "tb_topic")
public class TopicEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    /**
     * The state of the topic.
     * Must not be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TopicState state;

    /**
     * The forum to which the topic belongs.
     * Must not be null.
     */
    @ManyToOne
    @JoinColumn(name = "forum_id", nullable = false)
    private ForumEntity forum;

    /**
     * The creator of the topic.
     * Must not be null.
     */
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    /**
     * The comments associated with the topic.
     */
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    /**
     * The likes associated with the topic.
     */
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeEntity> likes = new ArrayList<>();

    /**
     * The timestamp when the topic was created.
     * Automatically set when the topic is created.
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    /**
     * The timestamp when the topic was last updated.
     * Automatically updated when the topic is modified.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Default constructor.
     * Initializes the topic with a new UUID and the current timestamp.
     */
    public TopicEntity() {
        Instant now = Instant.now();

        this.id = UUID.randomUUID();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Constructor with title, content, and state.
     * Initializes the topic with a new UUID and the current timestamp.
     *
     * @param title the title of the topic
     * @param content the content of the topic
     * @param state the state of the topic
     */
    public TopicEntity(String title, String content, TopicState state) {
        Instant now = Instant.now();

        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.state = state;
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Constructor with title, content, creator, and forum.
     * Initializes the topic with a new UUID and the current timestamp.
     *
     * @param title the title of the topic
     * @param content the content of the topic
     * @param creator the creator of the topic
     * @param forum the forum to which the topic belongs
     */
    public TopicEntity(String title, String content, UserEntity creator, ForumEntity forum) {
        Instant now = Instant.now();

        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.state = TopicState.OPEN;
        this.forum = forum;
        this.creator = creator;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TopicState getState() {
        return state;
    }

    public void setState(TopicState state) {
        this.state = state;
    }

    public ForumEntity getForum() {
        return forum;
    }

    public void setForum(ForumEntity forum) {
        this.forum = forum;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void addComments(CommentEntity comment) {
        if(!comments.contains(comment)) {
            comments.add(comment);
            comment.setTopic(this);
        }
    }

    public List<LikeEntity> getLikes() {
        return likes;
    }

    public void addLike(LikeEntity like) {
        if (!likes.contains(like)) {
            likes.add(like);
            like.setTopic(this);
        }
    }

    public void removeLike(LikeEntity like) {
        if (likes.contains(like)) {
            likes.remove(like);
            like.setTopic(null);
        }
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
