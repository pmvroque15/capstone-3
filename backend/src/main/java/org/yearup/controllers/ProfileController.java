package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class ProfileController {

    private ProfileService profileService;
    private UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Profile> getProfile(Principal principal) {
        String username = principal.getName();
        User user = userService.getByUserName(username);
        int userId = user.getId();
        Profile profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(Principal principal, @RequestBody Profile profile) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        Profile updateProfile = profileService.updateProfile(userId, profile);
        return ResponseEntity.ok(updateProfile);
    }
}
