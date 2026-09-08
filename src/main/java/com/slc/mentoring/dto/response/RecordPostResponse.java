package com.slc.mentoring.dto.response;

import com.slc.mentoring.entity.Drink;
import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecordPostResponse {
    private Long recordId;
    private Long firstMentorId;
    private String firstQuestion;
    private Long secondMentorId;
    private String secondQuestion;
    private Long thirdMentorId;
    private String thirdQuestion;
    private Drink drink;
    private LocalDateTime priorityAt;

    public RecordPostResponse(Record record) {
        this(record, "", "", "");
    }

    public RecordPostResponse(Record record, String firstQuestion, String secondQuestion, String thirdQuestion) {
        this.recordId = record.getRecordId();
        this.firstMentorId = mentorId(record.getFirstMentor());
        this.firstQuestion = emptyIfNull(firstQuestion);
        this.secondMentorId = mentorId(record.getSecondMentor());
        this.secondQuestion = emptyIfNull(secondQuestion);
        this.thirdMentorId = mentorId(record.getThirdMentor());
        this.thirdQuestion = emptyIfNull(thirdQuestion);
        this.drink = record.getDrink();
        this.priorityAt = record.getPriorityAt();
    }

    private static Long mentorId(Mentor mentor) {
        return mentor == null ? null : mentor.getMentorId();
    }

    private static String emptyIfNull(String value) {
        return value == null ? "" : value;
    }
}
