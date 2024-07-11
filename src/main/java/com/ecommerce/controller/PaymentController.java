package com.ecommerce.controller;

import com.ecommerce.entity.Payment;
import com.ecommerce.entity.User;
import com.ecommerce.jwt.JwtTokenProvider;
import com.ecommerce.repository.PaymentRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v5")
public class PaymentController {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private PaymentRepository paymentRepository;
    private PaymentService paymentService;
    private HistoryRepository historyRepository;

    @Autowired
    public PaymentController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,PaymentRepository paymentRepository,PaymentService paymentService,HistoryRepository historyRepository){
        this.userRepository=userRepository;
        this.jwtTokenProvider=jwtTokenProvider;
        this.paymentRepository=paymentRepository;
        this.paymentService = paymentService;
        this.historyRepository = historyRepository;
    }


    @PostMapping (value = "/payments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initiatePayment(@RequestHeader("Authorization") String jwt,@RequestBody Payment payment){
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            payment.setUser(user);
            this.paymentRepository.save(payment);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(value = "/payments",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> paymentStatus(@RequestHeader("Authorization") String jwt){
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            Payment payment = this.paymentRepository.findByUser(user);
            payment.setPaymentStatus(this.paymentService.paymentValidator());

            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
