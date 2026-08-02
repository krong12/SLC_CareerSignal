package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.VotePostRequest;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.dto.response.VoteGetResponse;
import com.slc.mentoring.dto.response.VotePostResponse;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @GetMapping("/vote")
    public ResponseEntity<VoteGetResponse> showVotes(
            @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        VoteGetResponse voteGetResponse = voteService.showVote(userInfo.getUserId());
        return ResponseEntity.ok(voteGetResponse);
    }

    @PostMapping("/vote")
    public ResponseEntity<VotePostResponse> CreateVote(@RequestBody VotePostRequest votePostRequest,
                                                       @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        VotePostResponse votePostResponse = voteService.CreateVote(userInfo.getUserId(), votePostRequest.getMentorId());
        return ResponseEntity.ok(votePostResponse);
    }

    @PostMapping("/favorite")
    public ResponseEntity<VotePostResponse> CreateFavorite(@RequestBody VotePostRequest votePostRequest,
                                                           @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        VotePostResponse votePostResponse = voteService.CreateFavorite(userInfo.getUserId(), votePostRequest.getMentorId());
        return ResponseEntity.ok(votePostResponse);
    }

    @DeleteMapping("/vote")
    public ResponseEntity<Void> DeleteVote(@RequestBody VotePostRequest votePostRequest,
                                           @SessionAttribute(name = "LOGIN_USER", required = false) UserPostResponse userInfo) {
        if(userInfo == null) throw new CustomException(ExceptionCode.NOT_LOGINED);
        voteService.DeleteVote(userInfo.getUserId(), votePostRequest.getMentorId());
        return ResponseEntity.noContent().build();
    }
}
