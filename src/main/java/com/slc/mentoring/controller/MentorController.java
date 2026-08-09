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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MentorController {
    private final MentorService mentorService;

    @GetMapping("/mentor")
    public ResponseEntity<MentorGetResponse> showMentors() {
        MentorGetResponse mentorGetResponse = mentorService.showMentors();
        return ResponseEntity.ok(mentorGetResponse);
    }

    @GetMapping("/mentorSearch")
    public ResponseEntity<MentorSearchResponse> searchMentors(MentorSearchRequest mentorSearchRequest) {
        MentorSearchResponse mentorSearchResponse = mentorService.searchMentors(mentorSearchRequest);
        return ResponseEntity.ok(mentorSearchResponse);
    }

    @PostMapping("/admin/mentor")
    public ResponseEntity<MentorPostResponse> createMentor(@RequestBody MentorPostRequest mentorPostRequest) {
        MentorPostResponse mentorPostResponse = mentorService.createMentor(mentorPostRequest);
        return ResponseEntity.ok(mentorPostResponse);
    }

    @DeleteMapping("/admin/mentor/{mentorId}")
    public ResponseEntity<Void> deleteMentor(@PathVariable Long mentorId) {
        mentorService.deleteMentor(mentorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/mentor/{mentorId}")
    public ResponseEntity<MentorPostResponse> updateMentor(@PathVariable Long mentorId, @RequestBody MentorPostRequest mentorPostRequest) {
        MentorPostResponse mentorPostResponse = mentorService.updateMentor(mentorId, mentorPostRequest);
        return ResponseEntity.ok(mentorPostResponse);
    }

    @PostMapping("/admin/mentor/batch")
    public ResponseEntity<Void> createMentorsByCSV(@RequestParam("file") MultipartFile file) {
        mentorService.createMentorsByCSV(file);
        return ResponseEntity.ok().build();
    }
}