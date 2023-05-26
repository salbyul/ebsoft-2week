package com.study.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardModifySaveDto {

    private final Long id;
    private final String writer;
    private final String title;
    private final String content;
}
