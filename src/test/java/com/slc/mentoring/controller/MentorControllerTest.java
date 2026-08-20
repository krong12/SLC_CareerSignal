package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.MentorPostRequest;
import com.slc.mentoring.dto.response.MentorGetResponse;
import com.slc.mentoring.dto.response.MentorPostResponse;
import com.slc.mentoring.dto.response.MentorSearchResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.entity.*;
import com.slc.mentoring.service.MentorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restdocs.test.autoconfigure.AutoConfigureRestDocs;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MentorController.class)
@AutoConfigureRestDocs
public class MentorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MentorService mentorService;

    private MockHttpSession adminSession;

    @BeforeEach
    void setup() {
        adminSession = new MockHttpSession();
        UserPostResponse loginUser = new UserPostResponse(1L, "admin");
        adminSession.setAttribute("LOGIN_USER", loginUser);
    }

    @Test
    @DisplayName("멘토 전체 조회")
    void showMentors() throws Exception {
        Major dummyMajor = new Major(1L, "경영학과");
        MentorPostResponse dummyMentor = new MentorPostResponse(
                1L,
                "홍길동",
                "냉철한 경제학과",
                List.of(dummyMajor),
                List.of(Field.SW),
                "삼성전자",
                "인턴",
                CareerPath.COMPANY,
                "서울",
                Area.CAPITAL_AREA,
                false,
                true,
                2020L,
                "안녕하세요, 백엔드 멘토 홍길동입니다.",
                "https://linkedin.com/in/gildong",
                null,
                true,
                true,
                MentorStatus.CONTACTING,
                5L,
                true,
                true,
                "모두들 건승하십시오."
        );
        MentorGetResponse mockResponse = new MentorGetResponse(List.of(dummyMentor));
        given(mentorService.showMentors()).willReturn(mockResponse);

        mockMvc.perform(get("/mentor")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("mentor/show-mentors",
                        responseFields(
                                fieldWithPath("mentorList").description("멘토 정보 리스트"),
                                fieldWithPath("mentorList[].mentorId").description("멘토 고유 ID"),
                                fieldWithPath("mentorList[].name").description("멘토 이름"),
                                fieldWithPath("mentorList[].alias").description("멘토 표시 이름"),

                                fieldWithPath("mentorList[].major").description("전공 목록"),
                                fieldWithPath("mentorList[].major[].majorId").description("전공 고유 ID"),
                                fieldWithPath("mentorList[].major[].name").description("전공 이름"),

                                fieldWithPath("mentorList[].field").description("활동 분야 목록"),
                                fieldWithPath("mentorList[].companyName").description("회사 이름"),
                                fieldWithPath("mentorList[].job").description("직무"),
                                fieldWithPath("mentorList[].careerPath").description("커리어 패스"),
                                fieldWithPath("mentorList[].areaName").description("지역 명칭"),
                                fieldWithPath("mentorList[].area").description("지역 정보"),
                                fieldWithPath("mentorList[].foreignSchool").description("해외 학교 여부"),
                                fieldWithPath("mentorList[].majorRelated").description("전공 관련 여부"),
                                fieldWithPath("mentorList[].graduatedYear").description("졸업 연도"),
                                fieldWithPath("mentorList[].introduce").description("소개글"),
                                fieldWithPath("mentorList[].linkedin").description("링크드인 주소"),
                                fieldWithPath("mentorList[].profileImagePath").description("프로필 이미지 경로"),
                                fieldWithPath("mentorList[].profileRelease").description("프로필 공개 여부"),
                                fieldWithPath("mentorList[].voteRelease").description("투표 공개 여부"),
                                fieldWithPath("mentorList[].mentorStatus").description("멘토 상태"),
                                fieldWithPath("mentorList[].mentorLimit").description("멘토 제한 인원"),
                                fieldWithPath("mentorList[].limitRelease").description("제한 공개 여부"),
                                fieldWithPath("mentorList[].remainRelease").description("잔여 인원 공개 여부"),
                                fieldWithPath("mentorList[].oneLine").description("멘토의 한마디")
                        )
                ));
    }

    @Test
    @DisplayName("멘토 필터링")
    void searchMentors() throws Exception {
        Major dummyMajor = new Major(1L, "경영학과");
        MentorPostResponse dummyMentor = new MentorPostResponse(
                1L,
                "홍길동",
                "냉철한 경제학과",
                List.of(dummyMajor),
                List.of(Field.SW),
                "삼성전자",
                "인턴",
                CareerPath.COMPANY,
                "서울",
                Area.CAPITAL_AREA,
                false,
                true,
                2020L,
                "안녕하세요, 백엔드 멘토 홍길동입니다.",
                "https://linkedin.com/in/gildong",
                null,
                true,
                true,
                MentorStatus.CONTACTING,
                5L,
                true,
                true,
                "모두들 건승하십시오."
        );
        MentorSearchResponse mockResponse = new MentorSearchResponse(List.of(dummyMentor));
        given(mentorService.searchMentors(any())).willReturn(mockResponse);

        mockMvc.perform(get("/mentorSearch")
                        .param("majorNames", "경영학과", "스포츠과학과")
                        .param("fields", "AI", "SW")
                        .param("careerPaths", "COMPANY")
                        .param("foreignSchools", "false")
                        .param("majorRelated", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("mentor/search-mentors",
                        queryParameters(
                                parameterWithName("majorNames").description("검색할 전공 이름 목록 (선택)").optional(),
                                parameterWithName("fields").description("검색할 활동 분야 목록 (선택)").optional(),
                                parameterWithName("careerPaths").description("검색할 커리어 패스 목록 (선택)").optional(),
                                parameterWithName("foreignSchools").description("해외 학교 여부 목록 (선택)").optional(),
                                parameterWithName("majorRelated").description("전공 관련 여부 목록 (선택)").optional()
                        ),
                        responseFields(
                                fieldWithPath("searchedMentors").description("검색된 멘토 정보 리스트"),
                                fieldWithPath("searchedMentors[].mentorId").description("멘토 고유 ID"),
                                fieldWithPath("searchedMentors[].name").description("멘토 이름"),
                                fieldWithPath("searchedMentors[].alias").description("멘토 표시 이름"),
                                fieldWithPath("searchedMentors[].major").description("전공 목록"),
                                fieldWithPath("searchedMentors[].major[].majorId").description("전공 고유 ID"),
                                fieldWithPath("searchedMentors[].major[].name").description("전공 이름"),
                                fieldWithPath("searchedMentors[].field").description("활동 분야 목록"),
                                fieldWithPath("searchedMentors[].companyName").description("회사 이름"),
                                fieldWithPath("searchedMentors[].job").description("직무"),
                                fieldWithPath("searchedMentors[].careerPath").description("커리어 패스"),
                                fieldWithPath("searchedMentors[].areaName").description("지역 명칭"),
                                fieldWithPath("searchedMentors[].area").description("지역 정보"),
                                fieldWithPath("searchedMentors[].foreignSchool").description("해외 학교 여부"),
                                fieldWithPath("searchedMentors[].majorRelated").description("전공 관련 여부"),
                                fieldWithPath("searchedMentors[].graduatedYear").description("졸업 연도"),
                                fieldWithPath("searchedMentors[].introduce").description("소개글"),
                                fieldWithPath("searchedMentors[].linkedin").description("링크드인 주소"),
                                fieldWithPath("searchedMentors[].profileImagePath").description("프로필 이미지 경로"),
                                fieldWithPath("searchedMentors[].profileRelease").description("프로필 공개 여부"),
                                fieldWithPath("searchedMentors[].voteRelease").description("투표 공개 여부"),
                                fieldWithPath("searchedMentors[].mentorStatus").description("멘토 상태"),
                                fieldWithPath("searchedMentors[].mentorLimit").description("멘토 제한 인원"),
                                fieldWithPath("searchedMentors[].limitRelease").description("제한 공개 여부"),
                                fieldWithPath("searchedMentors[].remainRelease").description("잔여 인원 공개 여부"),
                                fieldWithPath("searchedMentors[].oneLine").description("멘토의 한마디")
                        )
                ));

    }

    @Test
    @DisplayName("멘토 등록")
    void createMentor() throws Exception {
        Major dummyMajor = new Major(1L, "경영학과");
        MentorPostRequest request = new MentorPostRequest(
                "홍길동",
                "냉철한 경제학과",
                List.of("컴퓨터공학과"),
                List.of(Field.SW),
                "삼성전자",
                "인턴",
                CareerPath.COMPANY,
                "서울",
                Area.CAPITAL_AREA,
                false,
                true,
                2020L,
                "안녕하세요, 백엔드 멘토 홍길동입니다.",
                "https://linkedin.com/in/gildong",
                null,
                true,
                true,
                MentorStatus.CONTACTING,
                5L,
                true,
                true,
                "모두들 건승하십시오."
        );
        MentorPostResponse mockResponse = new MentorPostResponse(
                1L,
                "홍길동",
                "냉철한 경제학과",
                List.of(dummyMajor),
                List.of(Field.SW),
                "삼성전자",
                "인턴",
                CareerPath.COMPANY,
                "서울",
                Area.CAPITAL_AREA,
                false,
                true,
                2020L,
                "안녕하세요, 백엔드 멘토 홍길동입니다.",
                "https://linkedin.com/in/gildong",
                null,
                true,
                true,
                MentorStatus.CONTACTING,
                5L,
                true,
                true,
                "모두들 건승하십시오."
        );
        given(mentorService.createMentor(any())).willReturn(mockResponse);
        mockMvc.perform(post("/admin/mentor")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("mentor/create-mentor",
                        requestFields(
                                fieldWithPath("name").description("멘토 이름"),
                                fieldWithPath("alias").description("멘토 표시 이름"),
                                fieldWithPath("major").description("전공 이름 목록"),
                                fieldWithPath("field").description("활동 분야 목록"),
                                fieldWithPath("companyName").description("회사 이름"),
                                fieldWithPath("job").description("직무"),
                                fieldWithPath("careerPath").description("커리어 패스"),
                                fieldWithPath("areaName").description("지역 명칭"),
                                fieldWithPath("area").description("지역 정보 객체").optional(),
                                fieldWithPath("foreignSchool").description("해외 학교 여부"),
                                fieldWithPath("majorRelated").description("전공 관련 여부"),
                                fieldWithPath("graduatedYear").description("졸업 연도"),
                                fieldWithPath("introduce").description("소개글"),
                                fieldWithPath("linkedin").description("링크드인 주소"),
                                fieldWithPath("profileImagePath").description("프로필 이미지 경로"),
                                fieldWithPath("profileRelease").description("프로필 공개 여부"),
                                fieldWithPath("voteRelease").description("투표 공개 여부"),
                                fieldWithPath("mentorStatus").description("멘토 상태"),
                                fieldWithPath("mentorLimit").description("멘토 제한 인원"),
                                fieldWithPath("limitRelease").description("제한 공개 여부"),
                                fieldWithPath("remainRelease").description("잔여 인원 공개 여부"),
                                fieldWithPath("oneLine").description("멘토의 한마디")
                        ),
                        responseFields(
                                fieldWithPath("mentorId").description("멘토 고유 ID"),
                                fieldWithPath("name").description("멘토 이름"),
                                fieldWithPath("alias").description("멘토 표시 이름"),
                                fieldWithPath("major").description("전공 목록"),
                                fieldWithPath("major[].majorId").description("전공 고유 ID").optional(),
                                fieldWithPath("major[].name").description("전공 이름").optional(),
                                fieldWithPath("field").description("활동 분야 목록"),
                                fieldWithPath("companyName").description("회사 이름"),
                                fieldWithPath("job").description("직무"),
                                fieldWithPath("careerPath").description("커리어 패스"),
                                fieldWithPath("areaName").description("지역 명칭"),
                                fieldWithPath("area").description("지역 정보 객체").optional(),
                                fieldWithPath("foreignSchool").description("해외 학교 여부"),
                                fieldWithPath("majorRelated").description("전공 관련 여부"),
                                fieldWithPath("graduatedYear").description("졸업 연도"),
                                fieldWithPath("introduce").description("소개글"),
                                fieldWithPath("linkedin").description("링크드인 주소"),
                                fieldWithPath("profileImagePath").description("프로필 이미지 경로"),
                                fieldWithPath("profileRelease").description("프로필 공개 여부"),
                                fieldWithPath("voteRelease").description("투표 공개 여부"),
                                fieldWithPath("mentorStatus").description("멘토 상태"),
                                fieldWithPath("mentorLimit").description("멘토 제한 인원"),
                                fieldWithPath("limitRelease").description("제한 공개 여부"),
                                fieldWithPath("remainRelease").description("잔여 인원 공개 여부"),
                                fieldWithPath("oneLine").description("멘토의 한마디")
                        )
                ));
    }

    @Test
    @DisplayName("멘토 삭제")
    void deleteMentor() throws Exception {
        Long mentorId = 1L;
        mockMvc.perform(delete("/admin/mentor/{mentorId}", mentorId)
                        .session(adminSession))
                .andExpect(status().isNoContent())
                .andDo(document("mentor/delete-mentor",
                        pathParameters(
                                parameterWithName("mentorId").description("삭제할 멘토 고유 ID")
                        )
                ));
    }

    @Test
    @DisplayName("멘토 수정")
    void updateMentor() throws Exception {
        Long mentorId = 1L;
        Major dummyMajor = new Major(1L, "경영학과");
        MentorPostRequest request = new MentorPostRequest(
                "홍길동",
                "냉철한 경제학과",
                List.of("컴퓨터공학과"),
                List.of(Field.SW),
                "삼성전자",
                "인턴",
                CareerPath.COMPANY,
                "서울",
                Area.CAPITAL_AREA,
                false,
                true,
                2020L,
                "안녕하세요, 백엔드 멘토 홍길동입니다.",
                "https://linkedin.com/in/gildong",
                null,
                true,
                true,
                MentorStatus.CONTACTING,
                5L,
                true,
                true,
                "모두들 건승하십시오."
        );
        MentorPostResponse mockResponse = new MentorPostResponse(
                1L,
                "홍길동",
                "냉철한 경제학과",
                List.of(dummyMajor),
                List.of(Field.SW),
                "삼성전자",
                "인턴",
                CareerPath.COMPANY,
                "서울",
                Area.CAPITAL_AREA,
                false,
                true,
                2020L,
                "안녕하세요, 백엔드 멘토 홍길동입니다.",
                "https://linkedin.com/in/gildong",
                null,
                true,
                true,
                MentorStatus.CONTACTING,
                5L,
                true,
                true,
                "모두들 건승하십시오."
        );
        given(mentorService.updateMentor(eq(mentorId), any())).willReturn(mockResponse);
        mockMvc.perform(patch("/admin/mentor/{mentorId}", mentorId)
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("mentor/update-mentor",
                        pathParameters(
                                parameterWithName("mentorId").description("수정할 멘토 고유 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("멘토 이름"),
                                fieldWithPath("alias").description("멘토 표시 이름"),
                                fieldWithPath("major").description("전공 이름 목록"),
                                fieldWithPath("field").description("활동 분야 목록"),
                                fieldWithPath("companyName").description("회사 이름"),
                                fieldWithPath("job").description("직무"),
                                fieldWithPath("careerPath").description("커리어 패스"),
                                fieldWithPath("areaName").description("지역 명칭"),
                                fieldWithPath("area").description("지역 정보 객체").optional(),
                                fieldWithPath("foreignSchool").description("해외 학교 여부"),
                                fieldWithPath("majorRelated").description("전공 관련 여부"),
                                fieldWithPath("graduatedYear").description("졸업 연도"),
                                fieldWithPath("introduce").description("소개글"),
                                fieldWithPath("linkedin").description("링크드인 주소"),
                                fieldWithPath("profileImagePath").description("프로필 이미지 경로"),
                                fieldWithPath("profileRelease").description("프로필 공개 여부"),
                                fieldWithPath("voteRelease").description("투표 공개 여부"),
                                fieldWithPath("mentorStatus").description("멘토 상태"),
                                fieldWithPath("mentorLimit").description("멘토 제한 인원"),
                                fieldWithPath("limitRelease").description("제한 공개 여부"),
                                fieldWithPath("remainRelease").description("잔여 인원 공개 여부"),
                                fieldWithPath("oneLine").description("멘토의 한마디")
                        ),
                        responseFields(
                                fieldWithPath("mentorId").description("멘토 고유 ID"),
                                fieldWithPath("name").description("멘토 이름"),
                                fieldWithPath("alias").description("멘토 표시 이름"),
                                fieldWithPath("major").description("전공 목록"),
                                fieldWithPath("major[].majorId").description("전공 고유 ID").optional(),
                                fieldWithPath("major[].name").description("전공 이름").optional(),
                                fieldWithPath("field").description("활동 분야 목록"),
                                fieldWithPath("companyName").description("회사 이름"),
                                fieldWithPath("job").description("직무"),
                                fieldWithPath("careerPath").description("커리어 패스"),
                                fieldWithPath("areaName").description("지역 명칭"),
                                fieldWithPath("area").description("지역 정보 객체").optional(),
                                fieldWithPath("foreignSchool").description("해외 학교 여부"),
                                fieldWithPath("majorRelated").description("전공 관련 여부"),
                                fieldWithPath("graduatedYear").description("졸업 연도"),
                                fieldWithPath("introduce").description("소개글"),
                                fieldWithPath("linkedin").description("링크드인 주소"),
                                fieldWithPath("profileImagePath").description("프로필 이미지 경로"),
                                fieldWithPath("profileRelease").description("프로필 공개 여부"),
                                fieldWithPath("voteRelease").description("투표 공개 여부"),
                                fieldWithPath("mentorStatus").description("멘토 상태"),
                                fieldWithPath("mentorLimit").description("멘토 제한 인원"),
                                fieldWithPath("limitRelease").description("제한 공개 여부"),
                                fieldWithPath("remainRelease").description("잔여 인원 공개 여부"),
                                fieldWithPath("oneLine").description("멘토의 한마디")
                        )
                ));
    }

    @Test
    @DisplayName("CSV로 멘토 일괄 등록")
    void createMentorsByCSV() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "mentors.csv",
                "text/csv",
                "csv,data,sample".getBytes()
        );
        mockMvc.perform(multipart("/admin/mentor/batch")
                        .session(adminSession)
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("mentor/create-mentors-byCSV",
                        requestParts(
                                partWithName("file").description("업로드할 멘토 데이터가 담긴 CSV 파일")
                        )
                ));
    }
}
