package com.study.dto.board;

import lombok.Getter;

@Getter
public class PagingDto {
    private final int curPage;
    private final int rowSizePerPage = 10;
    private final int pageSize = 10;
    private final int totalRowCount;

    //    시작 번호
    private int firstRow;
    //    마지막 번호
    private int lastRow;
    //    총 페이지 수
    private int totalPageCount;
    //    시작 페이지 번호
    private int firstPage;
    //    마지막 페이지 번호
    private int lastPage;

    public void setPage() {

        totalPageCount = (totalRowCount - 1) / rowSizePerPage + 1;
        firstRow = (curPage - 1) * rowSizePerPage + 1;
        lastRow = firstRow + rowSizePerPage - 1;
        if (lastRow > totalRowCount) lastRow = totalRowCount;

        firstPage = (curPage - 1) / pageSize * pageSize + 1;
        lastPage = firstPage + pageSize - 1;
        if (lastPage > totalPageCount) lastPage = totalPageCount;
    }

    public PagingDto(int curPage, int totalRowCount) {
        this.curPage = curPage;
        this.totalRowCount = totalRowCount;
        setPage();
    }

}
