package com.slc.mentoring.service;

import com.slc.mentoring.dto.request.MentorPostRequest;
import com.slc.mentoring.dto.request.MentorSearchRequest;
import com.slc.mentoring.dto.response.MentorGetResponse;
import com.slc.mentoring.dto.response.MentorPostResponse;
import com.slc.mentoring.dto.response.MentorSearchResponse;
import com.slc.mentoring.entity.Area;
import com.slc.mentoring.entity.CareerPath;
import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.entity.MentorStatus;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
                .major(mentorPostRequest.getMajor())
                .field(mentorPostRequest.getField())
                .companyName(mentorPostRequest.getCompanyName())
                .job(mentorPostRequest.getJob())
                .careerPath(mentorPostRequest.getCareerPath())
                .area(mentorPostRequest.getArea())
                .foreignSchool(mentorPostRequest.isForeignSchool())
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
                mentorPostRequest.getMajor(),
                mentorPostRequest.getField(),
                mentorPostRequest.getCompanyName(),
                mentorPostRequest.getJob(),
                mentorPostRequest.getCareerPath(),
                mentorPostRequest.getArea(),
                mentorPostRequest.isForeignSchool(),
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
                    return req.equals(mentor.isForeignSchool());
                })
                .filter(mentor -> mentorSearchRequest.getMajorRelated() == null ||
                        mentorSearchRequest.getMajorRelated().equals(mentor.isMajorRelated()))
                .filter(mentor -> mentorSearchRequest.getGraduateYear() == null ||
                        mentorSearchRequest.getGraduateYear().equals(mentor.getGraduateYear()))
                .map(MentorPostResponse::new)
                .toList();
        return new MentorSearchResponse(searchedMentors);
    }

    public void createMentorsByCSV(MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("업로드한 파일이 비었습니다.");
        }
        List<MentorPostRequest> mentorPostRequests = new ArrayList<>();
        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();
            Iterable<CSVRecord> csvRecords = csvFormat.parse(fileReader);

            for(CSVRecord record : csvRecords) {
                String yearStr = record.get("graduatedYear");
                Long graduatedYear = (yearStr != null && !yearStr.isBlank() ? Long.valueOf(yearStr.trim()) : null);

                CareerPath careerPath = CareerPath.fromDescription(record.get("careerPath"));
                Area area = Area.fromDescription(record.get("area"));

                String foreignSchoolStr = record.get("foreignSchool").trim();
                boolean foreignSchool = foreignSchoolStr.equals("있음");

                String majorRelatedStr = record.get("majorRelated").trim();
                boolean majorRelated = majorRelatedStr.equals("매우 높음") || majorRelatedStr.equals("어느 정도 있음");

                String profileReleaseStr = record.get("profileRelease").trim();
                boolean profileRelease = profileReleaseStr.equals("동의");

                MentorPostRequest request = new MentorPostRequest(
                        record.get("name"),
                        record.get("major"),
                        record.get("field"),
                        record.get("companyName"),
                        record.get("job"),
                        careerPath,
                        area,
                        foreignSchool,
                        majorRelated,
                        graduatedYear,
                        record.get("introduction"),
                        record.get("linkedin"),
                        null,
                        profileRelease,
                        true,
                        MentorStatus.CONTACTING,
                        5L,
                        false,
                        false
                );
                mentorPostRequests.add(request);
            }

            for(MentorPostRequest request : mentorPostRequests) {
                createMentor(request);
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV 파일 파싱중 오류 발생 : " + e.getMessage(), e);
        }
    }
}
