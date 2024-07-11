package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    List<Product> findAllByProductIdIn(Set<String> productIdSet);
    List<Product> findAllByProductIdIn(List<String> productIdList);
    List<Product> findAllByUser(User user);
}
