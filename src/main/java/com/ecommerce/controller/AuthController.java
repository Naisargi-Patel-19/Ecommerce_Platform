package com.ecommerce.controller;

import com.ecommerce.entity.User;
import com.ecommerce.exception.UserAlreadyExist;
import com.ecommerce.exception.UserNotExist;
import com.ecommerce.jwt.JwtTokenProvider;
import com.ecommerce.reponse.AuthResponse;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.request.UserRequest;
import com.ecommerce.security.CustomUserDetailsService;
import com.ecommerce.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.ecommerce.constants.ExceptionConstant.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailsService customUserDetailsService;
    private ModelMapper modelMapper;
    private EmailService emailService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService, ModelMapper modelMapper,EmailService emailService,ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping(value = "/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserRequest userRequest) throws UserAlreadyExist {
        System.out.println(String.valueOf(userRequest));
        String email = userRequest.getEmail();
        String name = userRequest.getName();
        String password = userRequest.getPassword();

        Optional<User> optionalUser = this.userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            log.warn("mail Is Already Used With Another Account");
            throw new UserAlreadyExist(USER_ALREADY_EXIST);
        }

        User user = User.builder()
                        .name(name)
                        .email(email)
                        .password(this.passwordEncoder.encode(password))
                        .build();

        this.userRepository.save(user);
        log.info("user got created");

        Map<String, Object> model = new HashMap<>();
        model.put("Name","Naisargi");
        model.put("location","DAIICT,Gandhinagar");
        this.emailService.sendEmail(user.getEmail(),"Subject",model);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateJwtToken(authentication);

        AuthResponse authResponse = new AuthResponse(token, true);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        log.info(username +" ----- "+password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateJwtToken(authentication);
        AuthResponse authResponse= new AuthResponse();

        authResponse.setStatus(true);
        authResponse.setJwt(token);

        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
    }

    @DeleteMapping(value = "/user")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String jwt) throws UserNotExist {
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            this.userRepository.delete(user);
            return ResponseEntity.ok().build();
        }
        throw new UserNotExist(USER_NOT_EXIST);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        log.info("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            log.error("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.error("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
