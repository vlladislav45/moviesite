package com.jmovies.services.base;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface VideoStreamService {
    ResponseEntity<byte[]> prepareContent(String fileName, String fileType, String range);

    byte[] readByteRange(String filename, long start, long end) throws IOException;

    Long getFileSize(String fileName);
}
