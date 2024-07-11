package com.ecommerce.repository;

import com.ecommerce.entity.Payment;
import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,String> {

    Payment findByUser(User user);
}
