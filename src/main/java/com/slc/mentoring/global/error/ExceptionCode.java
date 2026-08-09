package com.slc.mentoring.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),

    NOT_FOUND_USER_ID(HttpStatus.NOT_FOUND, "유저 id를 찾을 수 없습니다."),
    NOT_MATCHED_PASSCODE(HttpStatus.UNAUTHORIZED, "패스코드가 일치하지 않습니다."),
    NOT_FOUND_MENTOR_ID(HttpStatus.NOT_FOUND, "멘토 id를 찾을 수 없습니다."),
    NOT_FOUND_MAJOR(HttpStatus.NOT_FOUND, "전공을 찾을 수 없습니다."),
    NOT_FOUND_VOTE_ID(HttpStatus.NOT_FOUND, "투표 id를 찾을 수 없습니다."),
    ALREADY_EXISTS_VOTE(HttpStatus.CONFLICT, "이미 투표한 멘토입니다."),
    TOO_MANY_VOTE(HttpStatus.BAD_REQUEST, "투표 횟수가 3회를 넘어섰습니다."),
    NOT_LOGINED(HttpStatus.UNAUTHORIZED, "로그인되지 않았습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");

    private final HttpStatus statusCode;
    private final String message;
}

