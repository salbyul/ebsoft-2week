package com.study.dto.board;

import lombok.Getter;

@Getter
public class BoardSearchDto {

    private Long id;
    private String category;
    private String title;
    private String writer;
    private Long views;
    private String createdDate;
    private String modifiedDate;
    private Boolean hasFile;

    public BoardSearchDto(Long id, String category, String title, String writer, Long views, String createdDate, String modifiedDate) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.writer = writer;
        this.views = views;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.hasFile = false;
    }

    public void setHasFile(Boolean hasFile) {
        this.hasFile = hasFile;
    }

    public boolean hasFile() {
        return this.hasFile;
    }
}
