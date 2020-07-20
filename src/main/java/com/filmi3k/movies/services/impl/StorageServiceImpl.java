package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.services.base.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class StorageServiceImpl implements StorageService {
    private static final String BASE_IMAGE_DIR = "./src/main/resources/static/images/";

    @Override
    public boolean store(MultipartFile file) {
        try {
            Files.write(Paths.get(BASE_IMAGE_DIR + file.getOriginalFilename() + ".jpg"), file.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Exception handling
            return false;
        }
    }

}
