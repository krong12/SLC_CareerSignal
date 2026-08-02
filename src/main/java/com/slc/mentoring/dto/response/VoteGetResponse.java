package com.slc.mentoring.dto.response;

import com.slc.mentoring.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VoteGetResponse {
    private List<Vote> votes;
}
