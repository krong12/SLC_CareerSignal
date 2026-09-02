package com.slc.mentoring.dto.response;

import com.slc.mentoring.entity.MatchResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultPostResponse {
    private Long resultId;
    private Long firstMentorId;
    private Long secondMentorId;

    public ResultPostResponse(MatchResult result) {
        this.resultId = result.getResultId();
        this.firstMentorId = result.getFirstMentor().getMentorId();
        this.secondMentorId = result.getSecondMentor().getMentorId();
    }
}
