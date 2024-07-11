package com.ecommerce.serviceimpl;

import com.ecommerce.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String paymentValidator() {
        Random random = new Random();
        boolean bool = random.nextBoolean();
        if(bool){
            return "Success";
        }
        return "Failed";
    }
}
