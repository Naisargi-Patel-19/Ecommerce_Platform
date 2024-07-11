package com.ecommerce.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistResponse {
    private Integer numberOfProducts;
    Set<ProductResponse> productResponses;
}
