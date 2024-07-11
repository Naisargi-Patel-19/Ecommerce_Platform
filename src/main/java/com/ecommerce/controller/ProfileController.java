package com.ecommerce.controller;

import com.ecommerce.entity.Profile;
import com.ecommerce.entity.User;
import com.ecommerce.jwt.JwtTokenProvider;
import com.ecommerce.repository.ProfileRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.ProfileRequest;
import com.ecommerce.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import static com.ecommerce.constants.ApiResponseMessage.*;

@Slf4j
@RestController
@RequestMapping(value = "api/v1")
public class ProfileController {
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private ProfileService profileService;
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private ProfileRepository profileRepository;

    @Autowired
    public ProfileController(ObjectMapper objectMapper, ProfileService profileService, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, ProfileRepository profileRepository,ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.profileService = profileService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/profiles", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> buildProfile(@RequestHeader(value = "Authorization", required = false) String jwt, @RequestParam(value = "file") MultipartFile file, @RequestPart(value = "profileData") HashMap<String, String> profileData) throws IOException, ParseException {
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String imgName = this.profileService.storeImage(file);
            Profile profile = this.objectMapper.convertValue(profileData, Profile.class);
            profile.setImgName(imgName);
            profile.setUser(user);
            this.profileRepository.save(profile);
            return ResponseEntity.ok(PROFILE_SAVE_SUCCESS);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(value = "/profiles/image", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<?> callProfileImage(@RequestHeader(value = "Authorization") String jwt) throws IOException {
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Profile profile = this.profileRepository.findByUser(user);
            byte[] imageBytes = this.profileService.fetchImage(profile.getImgName());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(value = "/profiles",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> callProfile(@RequestHeader("Authorization") String jwt){
       String username = this.jwtTokenProvider.getEmailFromToken(jwt);
       Optional<User> optionalUser = this.userRepository.findByEmail(username);

       if(optionalUser.isPresent()){
           User user = optionalUser.get();
           Profile profile = this.profileRepository.findByUser(user);
           ProfileRequest profileRequest = this.modelMapper.map(profile,ProfileRequest.class);
           return ResponseEntity.ok(profileRequest);
       }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping(value="/update/profiles")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization")String jwt,@RequestBody ProfileRequest updatedProfile){
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Profile profile = this.profileRepository.findByUser(user);
            profile.setName(updatedProfile.getName());
            profile.setPhone(updatedProfile.getPhone());
            profile.setAddress(updatedProfile.getAddress());
            this.profileRepository.save(profile);
            return ResponseEntity.ok(updatedProfile);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
