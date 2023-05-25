package com.study.dto.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileSaveDto {

    private String name;
    private String realName;
    private Long boardId;
}
