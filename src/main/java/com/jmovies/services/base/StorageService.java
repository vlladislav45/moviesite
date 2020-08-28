package com.jmovies.services.base;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    boolean store(MultipartFile file);

    /**
            * Store a file with custom path
     * path is relative to resources folder
     * path should NOT end with '/'
            * example file with name = Image.jpg and path = "static/images" will be stored in resources/static/images/Image.jpg
     * @param file the file to upload
     * @param path the path where the file will be stored
     * @return true if successful, otherwise else
            */
    boolean store(MultipartFile file, String path);

    /**
            * Store a file in chunks with given path, relative to resource folder
     * @param file the file
     * @param path the path relative to resource folder
     * @param chunkSize the chunkSize
     * @return true if successfull
     */
    boolean storeInChunks(MultipartFile file, String path, int chunkSize);
}
