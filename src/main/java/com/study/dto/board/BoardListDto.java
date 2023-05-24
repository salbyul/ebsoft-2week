package com.study.dto.board;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BoardListDto {
    private PagingDto pagingDto;
    private List<BoardSearchDto> boardSearchDtoList;
    private List<String> categories;
}
