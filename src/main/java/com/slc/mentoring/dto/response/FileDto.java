package com.slc.mentoring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@Getter
@AllArgsConstructor
public class FileDto {
    private final InputStream inputStream;
    private final String contentType;
}
