package com.ecommerce.serviceimpl;

import com.ecommerce.entity.Product;
import com.ecommerce.reponse.CartResponse;
import com.ecommerce.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CartResponse> productListings(List<Product> productList, Map<String,Integer> productIdMap) {

        List<CartResponse> cartResponseList = productList.stream()
                .map(product -> {
                    CartResponse cartResponse = modelMapper.map(product, CartResponse.class);
                    cartResponse.setProductQuantity(productIdMap.get(product.getProductId()));
                    return cartResponse;
                })
                .collect(Collectors.toList());

        return cartResponseList;
    }

}
