package com.filmi3k.movies.controllers;

import com.filmi3k.movies.services.base.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Uploads a file to the file system
     * TODO: Use StorageService inside various methods for different file uploads (example: upload movie, upload profile pic etc)
     *  Move to the proper controllers
     *  Update the entity status (ex: if user uploads profile photo, change profilePhotoUrl in database)
     *  Maybe add file limit and additional check for proper file types
     *  Write tests, test performance also
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file) {
        boolean isSuccess = this.storageService.store(file);
        if (!isSuccess)
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("error", "Could not write your file");
            }});
        return ResponseEntity.ok(new HashMap<String, String>() {{
            put("success", "File upload successfull");
        }});
    }
}
