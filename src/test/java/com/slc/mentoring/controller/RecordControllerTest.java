package com.slc.mentoring.controller;

import com.slc.mentoring.dto.request.RecordPostRequest;
import com.slc.mentoring.dto.response.RecordPostResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.entity.Drink;
import com.slc.mentoring.service.RecordService;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecordController.class)
@AutoConfigureRestDocs
public class RecordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecordService recordService;

    private MockHttpSession userSession;
    private RecordPostRequest recordPostRequest;
    private RecordPostResponse recordPostResponse;

    @BeforeEach
    void setup() {
        userSession = new MockHttpSession();
        UserPostResponse loginUser = new UserPostResponse(5L, "2025123456", "아무무");
        userSession.setAttribute("LOGIN_USER", loginUser);

        recordPostRequest = new RecordPostRequest(
                1L, "1지망 멘토에 대한 질문",
                2L, "2지망 멘토에 대한 질문",
                3L, "3지망 멘토에 대한 질문",
                Drink.ICETEA
        );

        recordPostResponse = new RecordPostResponse(
                1L, 1L, 2L, 3L, Drink.ICETEA, LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("레코드 조회 - 성공")
    void showRecords() throws Exception {
        given(recordService.showRecords(5L)).willReturn(recordPostResponse);

        mockMvc.perform(get("/record")
                        .session(userSession))
                .andExpect(status().isOk())
                .andDo(document("record-show",
                        responseFields(
                                fieldWithPath("recordId").description("레코드 ID"),
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID"),
                                fieldWithPath("thirdMentorId").description("3지망 멘토 ID"),
                                fieldWithPath("drink").description("선택한 음료"),
                                fieldWithPath("priorityAt").description("작성/우선순위 시간")
                        )
                ));
    }

    @Test
    @DisplayName("레코드 생성 - 성공")
    void createRecord() throws Exception {
        given(recordService.CreateRecord(eq(5L), any(RecordPostRequest.class))).willReturn(recordPostResponse);

        mockMvc.perform(post("/record")
                        .session(userSession)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(recordPostRequest)))
                .andExpect(status().isOk())
                .andDo(document("record-create",
                        requestFields(
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("firstQuestion").description("1지망 질문"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID"),
                                fieldWithPath("secondQuestion").description("2지망 질문"),
                                fieldWithPath("thirdMentorId").description("3지망 멘토 ID"),
                                fieldWithPath("thirdQuestion").description("3지망 질문"),
                                fieldWithPath("drink").description("선택한 음료")
                        ),
                        responseFields(
                                fieldWithPath("recordId").description("레코드 ID"),
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID"),
                                fieldWithPath("thirdMentorId").description("3지망 멘토 ID"),
                                fieldWithPath("drink").description("선택한 음료"),
                                fieldWithPath("priorityAt").description("작성/우선순위 시간")
                        )
                ));
    }

    @Test
    @DisplayName("레코드 수정 - 성공")
    void updateRecord() throws Exception {
        given(recordService.updateRecord(eq(5L), any(RecordPostRequest.class))).willReturn(recordPostResponse);

        mockMvc.perform(patch("/record")
                        .session(userSession)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(recordPostRequest)))
                .andExpect(status().isOk())
                .andDo(document("record-update",
                        requestFields(
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("firstQuestion").description("1지망 질문"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID"),
                                fieldWithPath("secondQuestion").description("2지망 질문"),
                                fieldWithPath("thirdMentorId").description("3지망 멘토 ID"),
                                fieldWithPath("thirdQuestion").description("3지망 질문"),
                                fieldWithPath("drink").description("선택한 음료")
                        ),
                        responseFields(
                                fieldWithPath("recordId").description("레코드 ID"),
                                fieldWithPath("firstMentorId").description("1지망 멘토 ID"),
                                fieldWithPath("secondMentorId").description("2지망 멘토 ID"),
                                fieldWithPath("thirdMentorId").description("3지망 멘토 ID"),
                                fieldWithPath("drink").description("선택한 음료"),
                                fieldWithPath("priorityAt").description("작성/우선순위 시간")
                        )
                ));
    }

    @Test
    @DisplayName("레코드 삭제 - 성공")
    void deleteRecord() throws Exception {
        doNothing().when(recordService).deleteRecord(5L);

        mockMvc.perform(delete("/record")
                        .session(userSession))
                .andExpect(status().isNoContent())
                .andDo(document("record-delete"));
    }
}