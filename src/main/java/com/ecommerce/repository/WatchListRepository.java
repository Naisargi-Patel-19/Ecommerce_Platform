package com.ecommerce.repository;

import com.ecommerce.entity.User;
import com.ecommerce.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WatchListRepository extends JpaRepository<WatchList,String> {
    WatchList findByUser(User user);
}
