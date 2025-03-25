package org.springframe.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframe.backend.utils.fileUploadUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class fileUploadController {
    private final fileUploadUtils fileUploadUtils;

    @PostMapping("/image")
    public Map<String,Object> upload(@RequestParam("file") MultipartFile file){
        try {
            String url = fileUploadUtils.upload(file);
            Map<String,Object> response = new HashMap<>();
            response.put("success",1);
            response.put("url",url);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
