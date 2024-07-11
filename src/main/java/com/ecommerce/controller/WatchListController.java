package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.WatchList;
import com.ecommerce.jwt.JwtTokenProvider;
import com.ecommerce.reponse.ProductResponse;
import com.ecommerce.reponse.WatchlistResponse;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.WatchListRepository;
import com.ecommerce.request.ProductRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v3")
public class WatchListController {
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private WatchListRepository watchListRepository;
    private ProductRepository productRepository;

    @Autowired
    public WatchListController(UserRepository userRepository,JwtTokenProvider jwtTokenProvider,WatchListRepository watchListRepository,ProductRepository productRepository,ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.watchListRepository = watchListRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/watchlists", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToWatchlist(@RequestHeader("Authorization") String jwt, @RequestBody List<ProductRequest> productRequestList){
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Set<String> productIdSet = productRequestList.stream().map(e->e.getProductId()).collect(Collectors.toSet());
            WatchList watchList = new WatchList();
            watchList.setProductIdSet(productIdSet);
            watchList.setUser(user);
            this.watchListRepository.save(watchList);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping(value = "/watchlists",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> callWatchlist(@RequestHeader("Authorization") String jwt){
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);
        WatchlistResponse watchlistResponse = new WatchlistResponse();

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            WatchList watchList = this.watchListRepository.findByUser(user);
            List<Product> productList = this.productRepository.findAllByProductIdIn(watchList.getProductIdSet());
            Set<ProductResponse> productResponseSet = productList.stream()
                    .map(product -> modelMapper.map(product, ProductResponse.class))
                    .collect(Collectors.toSet());

            //response is getting ready
            watchlistResponse.setProductResponses(productResponseSet);
            watchlistResponse.setNumberOfProducts(productResponseSet.size());
            return ResponseEntity.ok(watchlistResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }





}
