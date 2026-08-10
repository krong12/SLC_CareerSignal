package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.VotePostRequest;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.dto.response.VoteGetResponse;
import com.slc.mentoring.dto.response.VotePostResponse;
import com.slc.mentoring.service.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restdocs.test.autoconfigure.AutoConfigureRestDocs;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
@AutoConfigureRestDocs
public class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VoteService voteService;

    @Test
    @DisplayName("투표 목록 조회")
    void showVotes() throws Exception {
        UserPostResponse sessionUser = new UserPostResponse(1L, "20231234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", sessionUser);

        VoteGetResponse mockResponse = new VoteGetResponse(List.of());
        given(voteService.showVote(anyLong())).willReturn(mockResponse);

        mockMvc.perform(get("/vote")
                        .session(session)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("vote/show-votes",
                        responseFields(
                                fieldWithPath("votes").description("투표 정보 리스트")
                                // Vote 엔티티의 필드 구조에 따라 하위 필드가 있다면 votes[].fieldName 형태로 추가 필요
                        )
                ));
    }

    @Test
    @DisplayName("투표 생성")
    void createVote() throws Exception {
        UserPostResponse sessionUser = new UserPostResponse(1L, "20231234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", sessionUser);

        VotePostRequest request = new VotePostRequest(1L);
        VotePostResponse mockResponse = new VotePostResponse(1L, 1L, "홍길동");
        given(voteService.CreateVote(any(), any())).willReturn(mockResponse);

        mockMvc.perform(post("/vote")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("vote/create-vote",
                        requestFields(
                                fieldWithPath("mentorId").description("투표할 멘토 고유 ID")
                        ),
                        responseFields(
                                fieldWithPath("voteId").description("투표 고유 ID").optional(),
                                fieldWithPath("mentorId").description("멘토 고유 ID").optional(),
                                fieldWithPath("mentorName").description("멘토 이름").optional()
                        )
                ));
    }

    @Test
    @DisplayName("관심 등록 (즐겨찾기)")
    void createFavorite() throws Exception {
        UserPostResponse sessionUser = new UserPostResponse(1L, "20231234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", sessionUser);

        VotePostRequest request = new VotePostRequest(1L);
        VotePostResponse mockResponse = new VotePostResponse(1L, 1L, "홍길동");
        given(voteService.CreateFavorite(any(), any())).willReturn(mockResponse);

        mockMvc.perform(post("/favorite")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("vote/create-favorite",
                        requestFields(
                                fieldWithPath("mentorId").description("관심 등록할 멘토 고유 ID")
                        ),
                        responseFields(
                                fieldWithPath("voteId").description("관심/투표 고유 ID").optional(),
                                fieldWithPath("mentorId").description("멘토 고유 ID").optional(),
                                fieldWithPath("mentorName").description("멘토 이름").optional()
                        )
                ));
    }

    @Test
    @DisplayName("투표 취소(삭제)")
    void deleteVote() throws Exception {
        UserPostResponse sessionUser = new UserPostResponse(1L, "20231234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", sessionUser);

        Long mentorId = 1L;

        mockMvc.perform(delete("/vote/{mentorId}", mentorId)
                        .session(session))
                .andExpect(status().isNoContent())
                .andDo(document("vote/delete-vote",
                        pathParameters(
                                parameterWithName("mentorId").description("취소할 멘토 고유 ID")
                        )
                ));
    }

    @Test
    @DisplayName("특정 멘토 투표 수 조회")
    void getMentorVoteCount() throws Exception {
        Long mentorId = 1L;
        given(voteService.getMentorVoteCount(anyLong())).willReturn(5L);

        mockMvc.perform(get("/vote/mentor/{mentorId}", mentorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5L))
                .andDo(document("vote/get-mentor-vote-count",
                        pathParameters(
                                parameterWithName("mentorId").description("조회할 멘토 고유 ID")
                        )
                ));
    }

    @Test
    @DisplayName("잔여 투표권 수 조회")
    void getRemainingVoteCount() throws Exception {
        UserPostResponse sessionUser = new UserPostResponse(1L, "20231234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", sessionUser);

        given(voteService.getRemainVoteCount(anyLong())).willReturn(3L);

        mockMvc.perform(get("/vote/remain")
                        .session(session)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3L))
                .andDo(document("vote/get-remaining-vote-count"));
    }
}
