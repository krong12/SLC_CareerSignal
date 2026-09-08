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
    private Long secondMentorId;
    private Long thirdMentorId;
    private Drink drink;
    private LocalDateTime priorityAt;

    public RecordPostResponse(Record record) {
        this.recordId = record.getRecordId();
        this.firstMentorId = mentorId(record.getFirstMentor());
        this.secondMentorId = mentorId(record.getSecondMentor());
        this.thirdMentorId = mentorId(record.getThirdMentor());
        this.drink = record.getDrink();
        this.priorityAt = record.getPriorityAt();
    }

    private static Long mentorId(Mentor mentor) {
        return mentor == null ? null : mentor.getMentorId();
    }
}
