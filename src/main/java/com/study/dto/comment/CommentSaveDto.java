package com.study.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentSaveDto {

    private String writer;
    private String content;
    private Long boardId;
}
