package com.ecommerce.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    public List<String> uploadFiles(MultipartFile [] multipartFiles);
}
