package com.jmovies.controllers;

import com.jmovies.services.base.VideoStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoStreamController {
    private final VideoStreamService videoStreamService;

    @Autowired
    public VideoStreamController(VideoStreamService videoStreamService) {
        this.videoStreamService = videoStreamService;
    }

    @GetMapping("/stream/{fileType}/{fileName}")
    public ResponseEntity<byte[]> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                              @PathVariable("fileType") String fileType,
                                              @PathVariable("fileName") String fileName) {
        return videoStreamService.prepareContent(fileName, fileType, httpRangeList);
    }
}
