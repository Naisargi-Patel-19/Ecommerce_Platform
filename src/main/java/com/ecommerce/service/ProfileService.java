package com.ecommerce.service;

import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ProfileService {

    public String storeImage(MultipartFile file) throws IOException;

    public byte[] fetchImage(String imgName) throws IOException;

}
