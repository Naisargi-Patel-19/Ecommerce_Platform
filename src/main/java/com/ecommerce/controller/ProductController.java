package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.Profile;
import com.ecommerce.entity.User;
import com.ecommerce.jwt.JwtTokenProvider;
import com.ecommerce.reponse.CartResponse;
import com.ecommerce.reponse.ProductResponse;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.ecommerce.constants.ApiResponseMessage.*;


@RestController
@RequestMapping(value = "/api/v2")
public class ProductController {
    private ProductService productService;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private ProductRepository productRepository;

    @Autowired
    public ProductController(ProductService productService,JwtTokenProvider jwtTokenProvider,UserRepository userRepository,ModelMapper modelMapper,ProductRepository productRepository){
        this.productService = productService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }


    @PostMapping(value = "/products",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendAllProducts(@RequestHeader("Authorization") String jwt, @RequestParam("files") MultipartFile[] files, @RequestPart(value = "productDetail") HashMap<String,String> productRequest){
        String username = this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            List<String> imgPathList = this.productService.uploadFiles(files);
            Product product = this.modelMapper.map(productRequest,Product.class);
            product.setImgPaths(imgPathList);
            product.setUser(user);
            this.productRepository.save(product);
            return ResponseEntity.ok(PRODUCT_SAVE_SUCCESS);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/products",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProducts(@RequestHeader("Authorization") String jwt){
        String username=this.jwtTokenProvider.getEmailFromToken(jwt);
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Product> productList = this.productRepository.findAllByUser(user);
            List<ProductResponse> list=productList.stream().
                    map(product -> {
                        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
                        return productResponse;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.notFound().build();
    }


}
