package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.jwt.JwtTokenProvider;
import com.ecommerce.reponse.CartResponse;
import com.ecommerce.reponse.CartTotal;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.CartRequest;
import com.ecommerce.request.ProductRequest;
import com.ecommerce.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v4")
public class CartController {
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartService cartService;
    private ModelMapper modelMapper;

    @Autowired
    public CartController(JwtTokenProvider jwtTokenProvider,UserRepository userRepository,CartRepository cartRepository,ProductRepository productRepository, CartService cartService,ModelMapper modelMapper){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/carts",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String jwt, @RequestBody List<ProductRequest> productRequests){
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Product> productList = productRequests.stream()
                    .map(productRequest ->
                        this.modelMapper.map(productRequest,Product.class))
                    .collect(Collectors.toList());

            CartRequest cartRequest = new CartRequest();
            cartRequest.setProductList(productList);

            Map<String,Integer> productIdMap = new HashMap<>();

            cartRequest.getProductList().forEach(product -> {
                productIdMap.put(product.getProductId(),productIdMap.getOrDefault(product.getProductId(),0)+1);
            });

            Cart cart = new Cart();
            cart.setProductIdMap(productIdMap);
            cart.setUser(user);
            this.cartRepository.save(cart);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(value = "/carts",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> callCart(@RequestHeader("Authorization") String jwt){
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);
        CartTotal cartTotal = new CartTotal();

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Cart cart = this.cartRepository.findByUser(user);
            List<String> productIdList = cart.getProductIdMap().keySet()
                    .stream()
                    .collect(Collectors.toList());


            List<Product> productList = this.productRepository.findAllByProductIdIn(productIdList);
            List<CartResponse> cartResponseList = this.cartService.productListings(productList,cart.getProductIdMap());

            //setting value to cartTotal
            cartTotal.setCartResponseList(cartResponseList);
            cartTotal.setTotalPrice(
                    cartResponseList.stream()
                            .map(cartResponse -> cartResponse.getProductPrice().multiply(BigDecimal.valueOf(cartResponse.getProductQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            return ResponseEntity.ok(cartTotal);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
