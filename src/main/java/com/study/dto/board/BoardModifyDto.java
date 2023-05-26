package com.study.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardModifyDto {

    private final String category;
    private final String writer;
    private final String title;
    private final String content;
    private final Integer views;
    private final String createdDate;
    private final String modifiedDate;
    private List<String> fileList;

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
}
