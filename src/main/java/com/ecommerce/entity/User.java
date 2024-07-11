package com.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;
    private String name;
    private String email;
    private String password;
    private LocalDateTime localDateTime = LocalDateTime.now();


    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @OneToOne(mappedBy = "user" , orphanRemoval = true)
    private WatchList watchList;

    @OneToOne(mappedBy = "user",orphanRemoval = true)
    private Cart cart;

    @OneToOne(mappedBy = "user",orphanRemoval = true)
    private Payment payment;

}
