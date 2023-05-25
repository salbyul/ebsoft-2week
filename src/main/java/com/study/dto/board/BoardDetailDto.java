package com.study.dto.board;

import com.study.dto.FileDto;
import com.study.dto.comment.CommentDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardDetailDto {

    private final Long id;
    private final String category;
    private final String title;
    private final String writer;
    private final int views;
    private final String content;
    private final String createdDate;
    private final String modifiedDate;
    private List<FileDto> fileList = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();

    public BoardDetailDto(Long id, String category, String title, int views, String content, String createdDate, String modifiedDate, String writer) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.views = views;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.writer = writer;
    }

    public void setFileList(List<FileDto> fileDtos) {
        this.fileList = fileDtos;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

}
