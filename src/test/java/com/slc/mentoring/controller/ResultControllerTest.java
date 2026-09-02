package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.ResultPostRequest;
import com.slc.mentoring.dto.response.ResultPostResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.service.ResultService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResultController.class)
@AutoConfigureRestDocs
class ResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ResultService resultService;

    private MockHttpSession userSession;
    private ResultPostRequest resultPostRequest;
    private ResultPostResponse resultPostResponse;

    @BeforeEach
    void setup() {
        userSession = new MockHttpSession();
        UserPostResponse loginUser = new UserPostResponse(1L, "admin", "관리자");
        userSession.setAttribute("LOGIN_USER", loginUser);

        resultPostRequest = new ResultPostRequest(1L, 2L);

        resultPostResponse = new ResultPostResponse(1L, 1L, 2L);
    }

    @Test
    @DisplayName("결과 조회 - 성공")
    void showResult() throws Exception {
        given(resultService.showResult(1L)).willReturn(resultPostResponse);

        mockMvc.perform(get("/result")
                        .session(userSession))
                .andExpect(status().isOk())
                .andDo(document("result-show",
                        responseFields(
                                fieldWithPath("resultId").description("결과 ID"),
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID")
                        )
                ));
    }

    @Test
    @DisplayName("결과 생성 - 성공")
    void createResult() throws Exception {
        given(resultService.createResult(eq(1L), any(ResultPostRequest.class))).willReturn(resultPostResponse);

        mockMvc.perform(post("/admin/result")
                        .session(userSession)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(resultPostRequest)))
                .andExpect(status().isOk())
                .andDo(document("result-create",
                        requestFields(
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID")
                        ),
                        responseFields(
                                fieldWithPath("resultId").description("결과 ID"),
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID")
                        )
                ));
    }

    @Test
    @DisplayName("결과 수정 - 성공")
    void updateResult() throws Exception {
        given(resultService.updateResult(eq(1L), any(ResultPostRequest.class))).willReturn(resultPostResponse);

        mockMvc.perform(patch("/admin/result")
                        .session(userSession)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(resultPostRequest)))
                .andExpect(status().isOk())
                .andDo(document("result-update",
                        requestFields(
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID")
                        ),
                        responseFields(
                                fieldWithPath("resultId").description("결과 ID"),
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID")
                        )
                ));
    }

    @Test
    @DisplayName("결과 삭제 - 성공")
    void deleteResult() throws Exception {
        doNothing().when(resultService).deleteResult(1L);

        mockMvc.perform(delete("/result")
                        .session(userSession))
                .andExpect(status().isOk())
                .andDo(document("result-delete"));
    }

    @Test
    @DisplayName("CSV를 통한 결과 일괄 등록 - 성공")
    void createResultsByCSV() throws Exception {
        doNothing().when(resultService).createResultsByCSV(any(MockMultipartFile.class));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "results.csv",
                "text/csv",
                "userId,firstMentorId,secondMentorId\n5,1,2".getBytes()
        );

        mockMvc.perform(multipart("/admin/result/batch")
                        .file(file)
                        .session(userSession))
                .andExpect(status().isOk())
                .andDo(document("result-batch-create",
                        requestParts( // <-- 이 부분을 추가해 주세요!
                                partWithName("file").description("업로드할 CSV 파일 (userId, firstMentorId, secondMentorId)")
                        )
                ));
    }
}