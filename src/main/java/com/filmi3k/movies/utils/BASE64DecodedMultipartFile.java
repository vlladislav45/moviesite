package com.filmi3k.movies.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class BASE64DecodedMultipartFile implements MultipartFile {

    protected static final Logger log = LogManager.getLogger(BASE64DecodedMultipartFile.class);

    private byte[] imgContent;
    private String fileName;
    private String ext;

    public BASE64DecodedMultipartFile(byte[] imgContent, String file) {
        this.imgContent = imgContent;
        this.fileName = file.split("\\.")[0];
        this.ext = file.split("\\.")[1];
    }

    public String getExt() {
        return ext;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return fileName + "." + ext;
    }

    @Override
    public String getContentType() {
        if(getExt() == null) {
            return null;
        }
        return getExt();
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        try (FileOutputStream f = new FileOutputStream(dest)) {
            f.write(imgContent);
        }
    }
}
