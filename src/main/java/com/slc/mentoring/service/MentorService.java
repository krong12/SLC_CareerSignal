package com.slc.mentoring.service;

import com.slc.mentoring.dto.request.MentorPostRequest;
import com.slc.mentoring.dto.request.MentorSearchRequest;
import com.slc.mentoring.dto.response.MentorGetResponse;
import com.slc.mentoring.dto.response.MentorPostResponse;
import com.slc.mentoring.dto.response.MentorSearchResponse;
import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MentorService {
    private final MentorRepository mentorRepository;

    public MentorGetResponse showMentors() {
        List<Mentor> mentors = mentorRepository.findAll();
        List<MentorPostResponse> mentorList = mentors.stream()
                .map(MentorPostResponse::new)
                .toList();
        return new MentorGetResponse(mentorList);
    }

    public MentorPostResponse createMentor(MentorPostRequest mentorPostRequest) {
        Mentor mentor = Mentor.builder()
                .name(mentorPostRequest.getName())
                .companyName(mentorPostRequest.getCompanyName())
                .job(mentorPostRequest.getJob())
                .careerPath(mentorPostRequest.getCareerPath())
                .area(mentorPostRequest.getArea())
                .foreignSchool(mentorPostRequest.getForeignSchool())
                .majorRelated(mentorPostRequest.isMajorRelated())
                .graduateYear(mentorPostRequest.getGraduatedYear())
                .introduce(mentorPostRequest.getIntroduce())
                .linkedin(mentorPostRequest.getLinkedin())
                .profileImagePath(mentorPostRequest.getProfileImagePath())
                .profileRelease(mentorPostRequest.isProfileRelease())
                .voteRelease(mentorPostRequest.isVoteRelease())
                .mentorStatus(mentorPostRequest.getMentorStatus())
                .mentorLimit(mentorPostRequest.getMentorLimit())
                .limitRelease(mentorPostRequest.isLimitRelease())
                .remainRelease(mentorPostRequest.isRemainRelease())
                .build();
        Mentor savedMentor = mentorRepository.save(mentor);
        return new MentorPostResponse(savedMentor);
    }

    public void deleteMentor(Long mentorId) {
        mentorRepository.deleteById(mentorId);
    }

    public MentorPostResponse updateMentor(Long mentorId, MentorPostRequest mentorPostRequest) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));
        mentor.update(
                mentorPostRequest.getName(),
                mentorPostRequest.getCompanyName(),
                mentorPostRequest.getJob(),
                mentorPostRequest.getCareerPath(),
                mentorPostRequest.getArea(),
                mentorPostRequest.getForeignSchool(),
                mentorPostRequest.isMajorRelated(),
                mentorPostRequest.getGraduatedYear(),
                mentorPostRequest.getIntroduce(),
                mentorPostRequest.getLinkedin(),
                mentorPostRequest.getProfileImagePath(),
                mentorPostRequest.isProfileRelease(),
                mentorPostRequest.isVoteRelease(),
                mentorPostRequest.getMentorStatus(),
                mentorPostRequest.getMentorLimit(),
                mentorPostRequest.isLimitRelease(),
                mentorPostRequest.isRemainRelease()
        );
        return new MentorPostResponse(mentor);
    }

    public MentorSearchResponse searchMentors(MentorSearchRequest mentorSearchRequest) {
        List<MentorPostResponse> searchedMentors =  mentorRepository.findAll().stream()
                .filter(mentor -> mentorSearchRequest.getCareerPath() == null ||
                        mentor.getCareerPath() == mentorSearchRequest.getCareerPath())
                .filter(mentor -> mentorSearchRequest.getArea() == null ||
                        mentor.getArea() == mentorSearchRequest.getArea())
                .filter(mentor -> {
                    Boolean req = mentorSearchRequest.getForeignSchool();
                    if(req == null) return true;
                    if(req) return mentor.getForeignSchool() != null;
                    return mentor.getForeignSchool() == null;
                })
                .filter(mentor -> mentorSearchRequest.getMajorRelated() == null ||
                        mentorSearchRequest.getMajorRelated().equals(mentor.isMajorRelated()))
                .filter(mentor -> mentorSearchRequest.getGraduateYear() == null ||
                        mentorSearchRequest.getGraduateYear().equals(mentor.getGraduateYear()))
                .map(MentorPostResponse::new)
                .toList();
        return new MentorSearchResponse(searchedMentors);
    }
}
