package com.study.dto.comment;

import lombok.Getter;

@Getter
public class CommentDto {

    private Long id;
    private String writer;
    private String content;
    private String createdDate;

    public CommentDto(Long id, String writer, String content, String createdDate) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.createdDate = createdDate;
    }
}
