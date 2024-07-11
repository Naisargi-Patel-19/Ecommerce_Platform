package com.ecommerce.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartTotal {
    private BigDecimal totalPrice;
    private List<CartResponse> cartResponseList;
}
