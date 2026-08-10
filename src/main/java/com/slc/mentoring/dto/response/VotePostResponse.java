package com.slc.mentoring.dto.response;

import com.slc.mentoring.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class VotePostResponse {
    private Long voteId;
    private Long mentorId;
    private String mentorName;

    public VotePostResponse(Vote vote) {
        this.voteId = vote.getVoteId();
        this.mentorId = vote.getMentor().getMentorId();
        this.mentorName = vote.getMentor().getName();
    }
}
