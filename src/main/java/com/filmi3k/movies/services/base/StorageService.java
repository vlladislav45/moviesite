package com.filmi3k.movies.services.base;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    boolean store(MultipartFile file);
}
