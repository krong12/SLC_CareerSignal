package com.slc.mentoring.service;

import com.slc.mentoring.dto.request.VotePostRequest;
import com.slc.mentoring.dto.response.VoteGetResponse;
import com.slc.mentoring.dto.response.VotePostResponse;
import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.entity.User;
import com.slc.mentoring.entity.Vote;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.repository.MentorRepository;
import com.slc.mentoring.repository.UserRepository;
import com.slc.mentoring.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;

    public VoteGetResponse showVote(Long userId) {
        User user = userRepository.findById(userId) // 유효한 유저인지 확인
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        List<Vote> voteList = voteRepository.findAllByUser_UserId(userId);
        return new VoteGetResponse(voteList);
    }

    public VotePostResponse CreateVote(Long userId, Long mentorId) {
        User user = userRepository.findById(userId) // 유효한 유저인지 확인
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        Mentor mentor = mentorRepository.findById(mentorId) // 유효한 멘토인지 확인
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));

        if(voteRepository.existsByUserAndMentor(user, mentor)) {
            Vote tmp = voteRepository.findByUserAndMentor(user, mentor);
            if(!tmp.isFinal()) { // 이미 찜했으면 확정으로 변경
                tmp.update_vote();
                return new VotePostResponse(new Vote(user, mentor, true));
            }
            else { // 만약 기존에 같은 멘토에게 찜 혹은 투표를 한 이력이 있으면 에러 띄우기
                throw new CustomException(ExceptionCode.ALREADY_EXISTS_VOTE);
            }
        }

        // 투표를 3개 넘게 했는지 확인 필요
        Long count = voteRepository.countByUserAndIsFinal(user, true);
        if(count >= 3) {
            throw new CustomException(ExceptionCode.TOO_MANY_VOTE);
        }

        Vote vote = new Vote(user, mentor, true);
        voteRepository.save(vote);
        return new VotePostResponse(vote);
    }

    public VotePostResponse CreateFavorite(Long userId, Long mentorId) {
        User user = userRepository.findById(userId) // 유효한 유저인지 확인
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        Mentor mentor = mentorRepository.findById(mentorId) // 유효한 멘토인지 확인
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));

        if(voteRepository.existsByUserAndMentor(user, mentor)) { // 만약 기존에 같은 멘토에게 찜 혹은 투표를 한 이력이 있으면 에러 띄우기
            throw new CustomException(ExceptionCode.ALREADY_EXISTS_VOTE);
        }

        Vote vote = new Vote(user, mentor, false);
        Vote savedVote = voteRepository.save(vote);
        return new VotePostResponse(savedVote);
    }

    public void DeleteVote(Long userId, Long mentorId) {
        User user = userRepository.findById(userId) // 유효한 유저인지 확인
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        Mentor mentor = mentorRepository.findById(mentorId) // 유효한 멘토인지 확인
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));

        if(voteRepository.existsByUserAndMentor(user, mentor)) {
            Long voteId = voteRepository.findByUserAndMentor(user, mentor).getVoteId();
            voteRepository.deleteById(voteId);
        }
        return;
    }
}
