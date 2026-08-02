package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.MentorPostRequest;
import com.slc.mentoring.dto.request.MentorSearchRequest;
import com.slc.mentoring.dto.response.MentorGetResponse;
import com.slc.mentoring.dto.response.MentorPostResponse;
import com.slc.mentoring.dto.response.MentorSearchResponse;
import com.slc.mentoring.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MentorController {
    private final MentorService mentorService;

    @GetMapping("/mentor")
    public ResponseEntity<MentorGetResponse> showMentors() {
        MentorGetResponse mentorGetResponse = mentorService.showMentors();
        return ResponseEntity.ok(mentorGetResponse);
    }
    // 필터링에 따라 멘토 필터링해주는 기능 ## 필터링 요소에 관해 추가 논의 필요
    @GetMapping("/mentorSearch")
    public ResponseEntity<MentorSearchResponse> searchMentors(MentorSearchRequest mentorSearchRequest) {
        MentorSearchResponse mentorSearchResponse = mentorService.searchMentors(mentorSearchRequest);
        return ResponseEntity.ok(mentorSearchResponse);
    }

    @PostMapping("/admin/mentor")
    public ResponseEntity<MentorPostResponse> createMentor(MentorPostRequest mentorPostRequest) {
        MentorPostResponse mentorPostResponse = mentorService.createMentor(mentorPostRequest);
        return ResponseEntity.ok(mentorPostResponse);
    }

    @DeleteMapping("/admin/mentor/{mentorId}")
    public ResponseEntity<Void> deleteMentor(@PathVariable Long mentorId) {
        mentorService.deleteMentor(mentorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/mentor/{mentorId}")
    public ResponseEntity<MentorPostResponse> updateMentor(@PathVariable Long mentorId, MentorPostRequest mentorPostRequest) {
        MentorPostResponse mentorPostResponse = mentorService.updateMentor(mentorId, mentorPostRequest);
        return ResponseEntity.ok(mentorPostResponse);
    }
}