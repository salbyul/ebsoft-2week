package com.study.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardSaveDto {

    private String category;
    private String writer;
    private String password;
    private String title;
    private String content;
}
