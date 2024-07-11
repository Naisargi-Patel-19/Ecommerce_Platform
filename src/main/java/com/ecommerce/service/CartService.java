package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.reponse.CartResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CartService {

    List<CartResponse> productListings(List<Product> productList, Map<String,Integer> productIdMap);
}
