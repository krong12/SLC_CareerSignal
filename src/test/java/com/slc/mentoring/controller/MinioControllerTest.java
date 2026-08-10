package com.slc.mentoring.controller;

import com.slc.mentoring.dto.response.FileDto;
import com.slc.mentoring.service.MinioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restdocs.test.autoconfigure.AutoConfigureRestDocs;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MinioController.class)
@AutoConfigureRestDocs
public class MinioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MinioService minioService;

    @Test
    @DisplayName("파일 업로드")
    void uploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.png",
                MediaType.IMAGE_PNG_VALUE,
                "test image content".getBytes()
        );

        given(minioService.uploadFile(any())).willReturn("uuid-test-image.png");

        mockMvc.perform(multipart("/files/upload")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("file/upload-file",
                        requestParts(
                                partWithName("file").description("업로드할 파일")
                        )
                ));
    }

    @Test
    @DisplayName("파일 조회/다운로드")
    void viewFile() throws Exception {
        String fileName = "uuid-test-image.png";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test image content".getBytes());
        FileDto fileDto = new FileDto(inputStream, MediaType.IMAGE_PNG_VALUE);

        given(minioService.getFileWithMetadata(anyString())).willReturn(fileDto);

        mockMvc.perform(get("/files/view/{fileName}", fileName)
                        .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andDo(document("file/view-file",
                        pathParameters(
                                parameterWithName("fileName").description("조회할 파일명")
                        )
                ));
    }
}