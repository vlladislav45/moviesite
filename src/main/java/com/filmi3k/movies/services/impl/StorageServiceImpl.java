package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.services.base.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class StorageServiceImpl implements StorageService {
    private static final String TARGET_DIR = "./target/classes/";
    private static final String BASE_DIR = "./src/main/resources/";
    private static final String BASE_IMAGE_DIR = BASE_DIR + "static/profile-picture/";

    @Override
    public boolean store(MultipartFile file) {
        try {
            Files.write(
                    Paths.get(BASE_IMAGE_DIR + file.getOriginalFilename()),
                    file.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: exception handling and logging
            return false;
        }
    }

    @Override
    public boolean store(MultipartFile file, String path) {
        try {
            String imagePath = TARGET_DIR + path + "/";
            File f = new File(imagePath);
            f.mkdirs();
            Files.write(
                    Paths.get(imagePath + file.getOriginalFilename()),
                    file.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: exception handling and logging
            return false;
        }
    }

    @Override
    public boolean storeInChunks(MultipartFile file, String path, int chunkSize) {
        try {
            // TODO: Finish
            boolean flag = true;
            while(flag) {
                Files.write(
                        Paths.get(BASE_DIR + path + "/" + file.getOriginalFilename()),
                        file.getBytes(),
                        StandardOpenOption.APPEND);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: exception handling and logging
            return false;
        }
    }
}
