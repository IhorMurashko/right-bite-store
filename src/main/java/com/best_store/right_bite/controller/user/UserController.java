package com.best_store.right_bite.controller.user;

import com.best_store.right_bite.dto.user.BaseUserInfo;
import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.service.user.update.UpdatableUserService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
@Hidden
public class UserController {

    private final UpdatableUserService updatableUserService;
    private final UserCrudService userCrudService;

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/update-personal-info")
    public ResponseEntity<BaseUserInfo> updatePersonalUserInfo(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto,
                                                               Authentication authentication) {
        BaseUserInfo baseUserInfo = updatableUserService.updateUser(userUpdateRequestDto, authentication);
        return new ResponseEntity<>(baseUserInfo, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public ResponseEntity<BaseUserInfo> getCurrentUser(Authentication authentication) {
        BaseUserInfo baseUserInfo = updatableUserService.findUserBy(authentication);
        return new ResponseEntity<>(baseUserInfo, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<BaseUserInfo> getUserById(@PathVariable("id") Long id) {
        BaseUserInfo baseUserInfo = updatableUserService.findUserBy(id);
        return new ResponseEntity<>(baseUserInfo, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<BaseUserInfo> getUserByEmail(@PathVariable("email") String email) {
        BaseUserInfo baseUserInfo = updatableUserService.findUserBy(email);
        return new ResponseEntity<>(baseUserInfo, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete-current")
    public ResponseEntity<HttpStatus> deleteCurrentUser(Authentication authentication) {
        updatableUserService.deleteUserBy(authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete-by-email/{email}")
    public ResponseEntity<HttpStatus> deleteUserByEmail(@PathVariable String email) {
        userCrudService.deleteByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        userCrudService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}