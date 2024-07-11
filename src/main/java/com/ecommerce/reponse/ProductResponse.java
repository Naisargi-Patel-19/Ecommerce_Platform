package com.ecommerce.reponse;

import com.ecommerce.entity.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {
    private String productId;
    private String productName;
    private BigDecimal productPrice;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    private String productDisc;
}
