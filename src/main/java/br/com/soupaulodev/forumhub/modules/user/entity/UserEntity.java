package br.com.soupaulodev.forumhub.modules.user.entity;

import br.com.soupaulodev.forumhub.modules.comment.entity.CommentEntity;
import br.com.soupaulodev.forumhub.modules.forum.entity.ForumEntity;
import br.com.soupaulodev.forumhub.modules.like.entity.LikeEntity;
import br.com.soupaulodev.forumhub.modules.topic.entity.TopicEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


/**
 * Represents a user within the system.
 * <p>
 * The {@link UserEntity} class encapsulates the details of a user, including their personal
 * information (name, username, email), associated forums (both owned and participated in),
 * topics, comments, and likes. It also tracks the timestamps of user creation and updates.
 * </p>
 *
 * @author soupaulodev
 */
@Entity
@Table(name = "tb_user")
public class UserEntity {

    /**
     * Unique identifier of the user.
     */
    @Id
    private UUID id;

    /**
     * The full name of the user.
     * <p>
     * This is a required field and must not exceed 50 characters.
     * </p>
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * The unique username of the user.
     * <p>
     * This is a required field and must be unique across all users. It must not exceed 50 characters.
     * </p>
     */
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    /**
     * The email address of the user.
     * <p>
     * This is a required field and must be unique across all users.
     * </p>
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The password of the user, used for authentication.
     * <p>
     * This is a required field and must not be empty.
     * </p>
     */
    @Column(nullable = false)
    private String password;


    /**
     * The forums owned by the user.
     * <p>
     * A user can own multiple forums. These relationships are managed by the {@link ForumEntity} class.
     * </p>
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ForumEntity> ownedForums = new HashSet<>();

    /**
     * The forums in which the user is a participant.
     * <p>
     * A user can participate in multiple forums. These relationships are managed by the {@link ForumEntity} class.
     * </p>
     */
    @ManyToMany
    @JoinTable(
            name = "forum_participants",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "forum_id")
    )
    private Set<ForumEntity> participatingForums = new HashSet<>();

    /**
     * The topics created by the user.
     * <p>
     * A user can create multiple topics. These relationships are managed by the {@link TopicEntity} class.
     * </p>
     */
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TopicEntity> topics = new HashSet<>();

    /**
     * The comments made by the user.
     * <p>
     * A user can post multiple comments. These relationships are managed by the {@link CommentEntity} class.
     * </p>
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentEntity> comments = new HashSet<>();

    /**
     * The likes given by the user.
     * <p>
     * A user can give likes to various entities. These relationships are managed by the {@link LikeEntity} class.
     * </p>
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeEntity> likes = new HashSet<>();

    /**
     * The timestamp when the user was created.
     * <p>
     * This timestamp is automatically generated by the database when the user is first created.
     * </p>
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    /**
     * The timestamp when the user was last updated.
     * <p>
     * This timestamp is automatically updated whenever the user entity is modified.
     * </p>
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for initializing the user with a unique ID and creation timestamps.
     * <p>
     * This constructor assigns the current time to both the `createdAt` and `updatedAt` fields
     * and generates a new UUID for the user ID.
     * </p>
     */
    public UserEntity() {
        Instant now = Instant.now();

        this.id = UUID.randomUUID();

        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Constructor to create a user with the specified details.
     * <p>
     * This constructor initializes a user entity with the specified name, username, email, and password.
     * The username is automatically converted to lowercase and spaces are replaced with underscores.
     * </p>
     *
     * @param name the full name of the user
     * @param username the username of the user
     * @param email the email address of the user
     * @param password the password of the user
     */
    public UserEntity(String name, String username, String email, String password) {
        this();
        this.name = name;
        this.username = username.toLowerCase().replace(" ", "_");
        this.email = email;
        this.password = password;

        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user's unique identifier.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the new unique identifier to set.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the full name of the user.
     *
     * @return the user's full name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full name of the user.
     *
     * @param name the new full name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the username of the user.
     *
     * @return the user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the new email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the set of forums owned by the user.
     *
     * @return the set of forums owned by the user.
     */
    public Set<ForumEntity> getOwnedForums() {
        return ownedForums;
    }

    /**
     * Sets the set of forums owned by the user.
     *
     * @param ownedForums the new set of owned forums to set.
     */
    public void setOwnedForums(Set<ForumEntity> ownedForums) {
        this.ownedForums = ownedForums;
    }


    /**
     * Gets the set of forums the user is participating in.
     *
     * @return the set of forums the user is participating in.
     */
    public Set<ForumEntity> getParticipatingForums() {
        return participatingForums;
    }

    /**
     * Sets the set of forums the user is participating in.
     *
     * @param participatingForums the new set of participating forums to set.
     */
    public void setParticipatingForums(Set<ForumEntity> participatingForums) {
        this.participatingForums = participatingForums;
    }

    /**
     * Gets the set of topics created by the user.
     *
     * @return the set of topics created by the user.
     */
    public Set<TopicEntity> getTopics() {
        return topics;
    }

    /**
     * Sets the set of topics created by the user.
     *
     * @param topics the new set of topics to set.
     */
    public void setTopics(Set<TopicEntity> topics) {
        this.topics = topics;
    }/**
     * Gets the set of comments made by the user.
     *
     * @return the set of comments made by the user.
     */
    public Set<CommentEntity> getComments() {
        return comments;
    }

    /**
     * Sets the set of comments made by the user.
     *
     * @param comments the new set of comments to set.
     */
    public void setComments(Set<CommentEntity> comments) {
        this.comments = comments;
    }

    /**
     * Gets the set of likes given by the user.
     *
     * @return the set of likes given by the user.
     */
    public Set<LikeEntity> getLikes() {
        return likes;
    }

    /**
     * Sets the set of likes given by the user.
     *
     * @param likes the new set of likes to set.
     */
    public void setLikes(Set<LikeEntity> likes) {
        this.likes = likes;
    }

    /**
     * Gets the timestamp when the user was created.
     *
     * @return the timestamp when the user was created.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the user was created.
     *
     * @param createdAt the new timestamp to set for user creation.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the timestamp when the user was last updated.
     *
     * @return the timestamp when the user was last updated.
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the timestamp when the user was last updated.
     *
     * @param updatedAt the new timestamp to set for the last update of the user.
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Returns a string representation of the UserEntity object.
     * <p>
     * The string includes the user's ID, name, username, email, and the timestamps of creation and last update.
     * This is useful for logging and debugging purposes.
     * </p>
     *
     * @return a string representation of the UserEntity object.
     */
    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * Compares this UserEntity object to another object for equality.
     * <p>
     * This method checks whether the given object is an instance of {@link UserEntity}, and if so,
     * compares the ID of the two UserEntity objects for equality. If the IDs are the same, the objects are considered equal.
     * </p>
     *
     * @param obj the object to compare this UserEntity to.
     * @return true if the objects are equal (i.e., they have the same ID), false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Same object reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Different classes or null object
        }
        UserEntity that = (UserEntity) obj; // Cast to UserEntity
        return Objects.equals(this.id, that.id); // Compare IDs
    }

    /**
     * Returns a hash code value for the UserEntity object.
     * <p>
     * This method generates a hash code based on the user's ID, ensuring that equal objects (i.e., with the same ID)
     * have the same hash code. It is used in hashing data structures such as HashMap and HashSet.
     * </p>
     *
     * @return the hash code value of the UserEntity object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id); // Generate hash code based on the ID
    }
}
