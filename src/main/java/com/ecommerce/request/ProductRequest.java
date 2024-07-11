package com.ecommerce.request;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {
    private String productId;
    private String productName;
    private BigDecimal productPrice;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    private String productDisc;

    private List<String> imgPaths;

}
