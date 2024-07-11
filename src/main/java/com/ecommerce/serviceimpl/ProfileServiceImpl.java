package com.ecommerce.serviceimpl;

import com.ecommerce.constants.AppConstant;
import com.ecommerce.repository.ProfileRepository;
import com.ecommerce.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileServiceImpl implements ProfileService {
    private ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }



    @Override
    public String storeImage(MultipartFile file) throws IOException {
        String imageName = System.currentTimeMillis() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        File fileObj = new File(AppConstant.PROFILE_IMAGE_PATH);

        if(!fileObj.exists()){
            fileObj.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(AppConstant.PROFILE_IMAGE_PATH, imageName) );

        return imageName;
    }

    @Override
    public byte[] fetchImage(String imgName) throws IOException {
        String imagePath = AppConstant.PROFILE_IMAGE_PATH + imgName;

        // Load the image file
        Resource resource = new FileSystemResource(imagePath);
        if (!resource.exists()) {
            throw new RuntimeException("Image not found at path: " + imagePath);
        }

        // Read the image bytes
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }
}
