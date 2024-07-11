package com.ecommerce.serviceimpl;

import com.ecommerce.constants.AppConstant;
import com.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<String> uploadFiles(MultipartFile[] multipartFiles) {

        List<String> fileNameList = new ArrayList<>();

        File files = new File(AppConstant.PRODUCT_IMAGE_PATH);

        if(!files.exists()){
            files.mkdir();
        }

        Arrays.stream(multipartFiles).forEach(multipartFile -> {
            String originalFileName = multipartFile.getOriginalFilename();
            if (originalFileName != null) {
                String newFileName = System.currentTimeMillis() + originalFileName.substring(originalFileName.lastIndexOf("."));
                Path filePath = Paths.get(AppConstant.PRODUCT_IMAGE_PATH, newFileName);
                try {
                    Files.write(filePath, multipartFile.getBytes());
                    fileNameList.add(newFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return fileNameList;
    }
}
