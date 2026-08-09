package com.slc.mentoring.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Field {
    SEMICONDUCTOR("반도체"),
    DISPLAY("디스플레이"),
    AI("AI"),
    SW("SW"),
    ELECTRONICS("전자"),
    MACHINERY("기계"),
    AUTOMOTIVE("자동차"),
    BIOTECHNOLOGY("바이오"),
    CHEMICALS("화학"),
    ENERGY("에너지"),
    FINANCE("금융"),
    CONSULTING("컨설팅"),
    MARKETING("마케팅"),
    EDUCATION("교육"),
    LEGAL_SERVICE("법률"),
    HEALTHCARE("의료"),
    PUBLIC_INSTITUTION("공공기관"),
    RESEARCH_INSTITUTION("연구기관"),
    STARTUP("스타트업"),
    ETC("기타");

    private final String description;
    Field(String description) { this.description = description; }
    public String getDescription() { return description; }

    public static Field fromDescription(String description) {
        if(description == null || description.isBlank()) return ETC;
        return Arrays.stream(Field.values())
                .filter(field -> field.description.equals(description.trim()))
                .findFirst()
                .orElse(Field.ETC);
    }

    public static List<Field> fromDescriptionList(String descriptionList) {
        if(descriptionList == null || descriptionList.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.stream(descriptionList.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Field::fromDescription)
                .collect(Collectors.toList());
    }
}
