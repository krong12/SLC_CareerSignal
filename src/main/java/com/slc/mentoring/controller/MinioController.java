package com.slc.mentoring.controller;

import com.slc.mentoring.dto.response.FileDto;
import com.slc.mentoring.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class MinioController {
    private final MinioService minioService;

    @PostMapping("/files/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = minioService.uploadFile(file);
        return ResponseEntity.ok("업로드 완료, 파일명: " + fileName);
    }

    @GetMapping("/files/view/{fileName}")
    public ResponseEntity<InputStreamResource> viewFile(@PathVariable("fileName") String fileName) {
        FileDto fileDto = minioService.getFileWithMetadata(fileName);
        String contentType = fileDto.getContentType();
        InputStream inputStream = fileDto.getInputStream();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(inputStream));
    }
}
