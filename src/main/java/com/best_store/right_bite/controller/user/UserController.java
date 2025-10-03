package com.best_store.right_bite.controller.user;

import com.best_store.right_bite.dto.user.BaseUserInfo;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.service.user.update.UpdatableUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "User controller",
        description = "user's default operation" +
                "all this method are secured and for request to this controller needs " +
                "JWT token in the header."
)
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UpdatableUserService updatableUserService;
    private final UserCrudService userCrudService;

    @Operation(
            summary = "Update current user info",
            description = "Partially updates fields of the currently authenticated user. User is identified from JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Fields to update",
                    content = @Content(schema = @Schema(implementation = UserUpdateRequestDto.class))
            ),
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            }
    )
    @PreAuthorize("isAuthenticated()")
    @PatchMapping
    public ResponseEntity<BaseUserInfo> updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                                   @AuthenticationPrincipal JwtPrincipal principal) {
        BaseUserInfo baseUserInfo = updatableUserService.updateUser(userUpdateRequestDto, principal);
        return new ResponseEntity<>(baseUserInfo, HttpStatus.OK);
    }

    @Operation(
            summary = "Get current user",
            description = "Returns the user data extracted from JWT token.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Current user data",
                            content = @Content(schema = @Schema(implementation = DefaultUserInfoResponseDto.class)))
            }
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public ResponseEntity<BaseUserInfo> getCurrentUser(@AuthenticationPrincipal JwtPrincipal principal) {
        BaseUserInfo baseUserInfo = userCrudService.findById(Long.parseLong(principal.id()));
        return new ResponseEntity<>(baseUserInfo, HttpStatus.OK);
    }

    @Operation(
            summary = "Find user by ID",
            description = "Returns user by their unique ID.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-id/{id}")
    public ResponseEntity<BaseUserInfo> getUserById(@PathVariable("id") Long id) {
        BaseUserInfo baseUserInfo = userCrudService.findById(id);
        return new ResponseEntity<>(baseUserInfo, HttpStatus.OK);
    }

    @Operation(
            summary = "Find user by email",
            description = "Returns user by their unique email address.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-email/{email}")
    public ResponseEntity<BaseUserInfo> getUserByEmail(@PathVariable("email") String email) {
        BaseUserInfo baseUserInfo = userCrudService.findByEmail(email);
        return new ResponseEntity<>(baseUserInfo, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete current user",
            description = "Deletes the authenticated user based on the JWT token.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteCurrentUser(@AuthenticationPrincipal JwtPrincipal principal) {
        userCrudService.deleteById(Long.parseLong(principal.id()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Delete user by email",
            description = "Deletes a user by their email address.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/by-email/{email}")
    public ResponseEntity<HttpStatus> deleteUserByEmail(@PathVariable("email") String email) {
        userCrudService.deleteByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Deletes a user by their unique ID.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/by-id/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        userCrudService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}