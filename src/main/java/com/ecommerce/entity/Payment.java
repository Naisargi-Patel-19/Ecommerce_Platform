package com.ecommerce.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Transient
    private String paymentStatus;

    private BigDecimal totalPrice;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "purchases in payment",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "purchase_id")
    )
    private List<Purchase> purchaseList;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
