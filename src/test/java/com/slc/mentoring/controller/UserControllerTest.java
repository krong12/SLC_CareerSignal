package com.slc.mentoring.controller;

import org.springframework.mock.web.MockMultipartFile;
import tools.jackson.databind.ObjectMapper;
import com.slc.mentoring.dto.request.UserPostRequest;
import com.slc.mentoring.dto.response.UserGetResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restdocs.test.autoconfigure.AutoConfigureRestDocs;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("관리자 여부 확인")
    void amIadmin() throws Exception {
        UserPostResponse sessionUser = new UserPostResponse(1L, "2026123456");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER", sessionUser);

        mockMvc.perform(get("/admin/check")
                        .session(session)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/am-i-admin",
                        responseBody()
                ));
    }

    @Test
    @DisplayName("유저 등록")
    void signup() throws Exception {
        UserPostRequest request = new UserPostRequest("2026123456", "password1234");
        UserPostResponse mockResponse = new UserPostResponse(1L, "2026123456");
        given(userService.signup(any())).willReturn(mockResponse);

        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/signup",
                        requestFields(
                                fieldWithPath("studentId").description("학번"),
                                fieldWithPath("passCode").description("패스코드")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("유저 고유 ID"),
                                fieldWithPath("studentId").description("학번")
                        )
                ));
    }

    @Test
    @DisplayName("유저 전체 조회")
    void showUsers() throws Exception {
        UserPostResponse userResponse = new UserPostResponse(1L, "2026123456");
        UserGetResponse mockResponse = new UserGetResponse(List.of(userResponse));
        given(userService.showUsers()).willReturn(mockResponse);

        mockMvc.perform(get("/admin/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/show-users",
                        responseFields(
                                fieldWithPath("user_list").description("유저 정보 리스트"),
                                fieldWithPath("user_list[].userId").description("유저 고유 ID"),
                                fieldWithPath("user_list[].studentId").description("학번")
                        )
                ));
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteUser() throws Exception {
        Long userId = 1L;
        mockMvc.perform(delete("/admin/user/{userId}", userId))
                .andExpect(status().isNoContent())
                .andDo(document("user/delete-user",
                        pathParameters(
                                parameterWithName("userId").description("삭제할 유저 고유 ID")
                        )
                ));
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        UserPostRequest request = new UserPostRequest("2026123456", "password1234");
        UserPostResponse mockResponse = new UserPostResponse(1L, "2026123456");
        given(userService.login(any())).willReturn(mockResponse);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/login",
                        requestFields(
                                fieldWithPath("studentId").description("학번"),
                                fieldWithPath("passCode").description("패스코드")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("유저 고유 ID"),
                                fieldWithPath("studentId").description("학번")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andDo(document("user/logout"));
    }

    @Test
    @DisplayName("CSV로 유저 일괄 등록")
    void createUsersByCSV() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "users.csv",
                "text/csv",
                "csv,data,sample".getBytes()
        );
        mockMvc.perform(multipart("/admin/user/batch")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/create-users-byCSV",
                        requestParts(
                                partWithName("file").description("업로드할 유저 데이터가 담긴 CSV 파일")
                        )
                ));
    }
}
