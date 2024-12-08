package br.com.soupaulodev.forumhub.modules.user.controller;

import br.com.soupaulodev.forumhub.modules.user.controller.dto.UserResponseDTO;
import br.com.soupaulodev.forumhub.modules.user.controller.dto.UserUpdateRequestDTO;
import br.com.soupaulodev.forumhub.modules.user.usecase.DeleteUserUsecase;
import br.com.soupaulodev.forumhub.modules.user.usecase.FindUserByNameOrUsernameUsecase;
import br.com.soupaulodev.forumhub.modules.user.usecase.GetUserUseCase;
import br.com.soupaulodev.forumhub.modules.user.usecase.UpdateUserUsecase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for handling user-related operations.
 * This class provides endpoints for retrieving by ID, finding by name or username, updating, and deleting users.
 * The user-related operations are managed by interacting with the use cases retrieving by ID, finding by name or username,
 * updating, and deleting users.
 *
 * <p>
 * The {@link UserController} is responsible for:
 * <ul>
 *     <li>Handling user retrieval requests by ID.</li>
 *     <li>Handling user retrieval requests by name or username.</li>
 *     <li>Handling user update requests.</li>
 *     <li>Handling user deletion requests.</li>
 * </ul>
 * </p>
 *
 * @author <a href="https://soupaulodev.com.br">soupaulodev</a>
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final GetUserUseCase getUserUsecase;
    private final FindUserByNameOrUsernameUsecase findUserByNameOrUsernameUsecase;
    private final UpdateUserUsecase updateUserUsecase;
    private final DeleteUserUsecase deleteUserUsecase;

    /**
     * Constructor for {@link UserController}.
     *
     * @param getUserUsecase {@link GetUserUseCase} the use case for handling user retrieval by ID
     * @param findUserByNameOrUsernameUsecase {@link FindUserByNameOrUsernameUsecase} the use case for handling user retrieval by name or username
     * @param updateUserUsecase {@link UpdateUserUsecase} the use case for handling user update operations
     * @param deleteUserUsecase {@link DeleteUserUsecase} the use case for handling user deletion operations
     */
    public UserController(GetUserUseCase getUserUsecase,
                          FindUserByNameOrUsernameUsecase findUserByNameOrUsernameUsecase,
                          UpdateUserUsecase updateUserUsecase,
                          DeleteUserUsecase deleteUserUsecase) {
        this.getUserUsecase = getUserUsecase;
        this.findUserByNameOrUsernameUsecase = findUserByNameOrUsernameUsecase;
        this.updateUserUsecase = updateUserUsecase;
        this.deleteUserUsecase = deleteUserUsecase;
    }

    /**
     * Endpoint for handling retrieval of a user by their unique identifier.
     * This method retrieves a user by their unique identifier and returns the user data.
     *
     * @param id the user's unique identifier of type {@link UUID}
     * @return a {@link ResponseEntity} of {@link UserResponseDTO} with status 200 (OK) and the user data
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") UUID id) {

        return ResponseEntity.ok(getUserUsecase.execute(id));
    }

    /**
     * Endpoint for handling retrieval of a user by their name or username.
     * This method retrieves a user by their name or username and returns the user data.
     *
     * @param query the name or username of the {@link String} type user to be retrieved
     * @return a {@link ResponseEntity} of {@link UserResponseDTO} with status 200 (OK) and the user data
     */
    @GetMapping("/find/{nameOrUsername}")
    public ResponseEntity<UserResponseDTO> getUserByNameOrUsername(@PathVariable("nameOrUsername") String query) {

        return ResponseEntity.ok(findUserByNameOrUsernameUsecase.execute(query));
    }

    /**
     * Endpoint for handling user update operations.
     * This method updates a user by their unique identifier, using the data provided in the request DTO
     * and returns the updated user data.
     *
     * @param id the unique identifier of the user to be updated
     * @param requestDTO the data transfer object {@link UserUpdateRequestDTO} containing user update data
     * @return a {@link ResponseEntity} of {@link UserResponseDTO} with status 200 (OK) and the updated user data
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") UUID id,
                                      @Valid @RequestBody UserUpdateRequestDTO requestDTO) {

        return ResponseEntity.ok(updateUserUsecase.execute(id, requestDTO));
    }

    /**
     * Endpoint for handling user deletion operations.
     * This method deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     * @return a {@link ResponseEntity} of {@link Void} with status 204 (NO_CONTENT) to indicate the successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {

        deleteUserUsecase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
