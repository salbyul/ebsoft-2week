package com.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class SearchDto {

    private final Integer offset;
    private final String startDate;
    private final String endDate;
    private final String category;
    private final String search;
}
