package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.RecordPostRequest;
import com.slc.mentoring.dto.response.RecordPostResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @GetMapping("/record")
    public ResponseEntity<RecordPostResponse> showRecords(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        RecordPostResponse response = recordService.showRecords(userInfo.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/record")
    public ResponseEntity<RecordPostResponse> createRecord(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo,
            @RequestBody RecordPostRequest request) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        RecordPostResponse response = recordService.CreateRecord(userInfo.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/record")
    public ResponseEntity<RecordPostResponse> updateRecord(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo,
            @RequestBody RecordPostRequest request) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        RecordPostResponse response = recordService.updateRecord(userInfo.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/record")
    public ResponseEntity<Void> deleteRecord(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        recordService.deleteRecord(userInfo.getUserId());
        return ResponseEntity.noContent().build();
    }
}
