package com.ecommerce.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.usertype.LoggableUserType;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "watchlists")
public class WatchList {
    @Id
    @Column(name = "watchlist_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String watchlistId;

    @ElementCollection
    @CollectionTable(name = "Products_in_watchlist",joinColumns = @JoinColumn(name = "product_ids"))
    private Set<String> productIdSet;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
