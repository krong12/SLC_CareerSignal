package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.ResultPostRequest;
import com.slc.mentoring.dto.response.ResultPostResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @GetMapping("/result")
    public ResponseEntity<ResultPostResponse> showResult(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        ResultPostResponse response = resultService.showResult(userInfo.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/result")
    public ResponseEntity<ResultPostResponse> createResult(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo,
            @RequestBody ResultPostRequest request) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        ResultPostResponse response = resultService.createResult(userInfo.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/admin/result")
    public ResponseEntity<ResultPostResponse> updateResult(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo,
            @RequestBody ResultPostRequest request) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        ResultPostResponse response = resultService.updateResult(userInfo.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/result")
    public ResponseEntity<Void> deleteResult(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        resultService.deleteResult(userInfo.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/result/batch")
    public ResponseEntity<Void> createResultsByCSV(@RequestParam MultipartFile file) {
        resultService.createResultsByCSV(file);
        return ResponseEntity.ok().build();
    }
}
