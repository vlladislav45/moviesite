package com.jmovies.services.impl;

import com.jmovies.services.base.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class StorageServiceImpl implements StorageService {
    private static final String TARGET_DIR = "./target/classes/";
    private static final String BASE_DIR = "./src/main/resources/";
    private static final String BASE_IMAGE_DIR = BASE_DIR + "static/profile-picture/";
    private static final String BASE_MOVIE_DIR = BASE_DIR + "static/movies/";

    private Map<String, OutputStream> openStreams;

    public StorageServiceImpl() {
        this.openStreams = new HashMap<>();
    }

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
    public boolean storeInChunks(MultipartFile file, String path, boolean isStart) {
        try {
            String filePath = BASE_MOVIE_DIR + path;
            if (isStart) {
                Files.write(Paths.get(filePath),
                        file.getBytes());
            } else {
                Files.write(
                        Paths.get(filePath),
                        file.getBytes(),
                        StandardOpenOption.WRITE,
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
