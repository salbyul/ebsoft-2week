package com.study.service.board;

import com.study.dto.SearchDto;
import com.study.dto.board.BoardListDto;
import com.study.dto.board.BoardSearchDto;
import com.study.dto.board.PagingDto;
import com.study.util.WebUtil;
import com.study.validator.IndexValidator;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BoardListService implements BoardService {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardListService EXECUTE");

        BoardListDto boardListDto = new BoardListDto();
        WebUtil webUtil = new WebUtil();
        Optional<String> offset = Optional.ofNullable(request.getParameter("offset"));
        Optional<String> startDate = Optional.ofNullable(request.getParameter("start_date"));
        Optional<String> endDate = Optional.ofNullable(request.getParameter("end_date"));
        Optional<String> category = Optional.ofNullable(request.getParameter("category"));
        Optional<String> search = Optional.ofNullable(request.getParameter("search"));

        try {
            List<String> categories = boardRepository.getCategories();
            IndexValidator.IndexValidatorBuilder builder = new IndexValidator.IndexValidatorBuilder(categories);
            IndexValidator indexValidator = builder.setOffset(Integer.parseInt(offset.orElse("0")))
                    .setStartDate(startDate.orElse(""))
                    .setEndDate(endDate.orElse(""))
                    .setCategory(category.orElse(""))
                    .setSearch(search.orElse(""))
                    .build();

            SearchDto searchDto = indexValidator.getSearchDto();
            request.setAttribute("categories", categories);
            List<BoardSearchDto> boardList = boardRepository.findBoardPaging(searchDto, 10);
            Integer count = boardRepository.findCountBySearchDto(searchDto);
            boardListDto.setBoardSearchDtoList(boardList);
            boardListDto.setPagingDto(new PagingDto(Integer.parseInt(Optional.ofNullable(request.getParameter("page")).orElse("0")), count));
            request.setAttribute("boardListDto", boardListDto);

            String nextPath = webUtil.assembleNextPath(request, "index.jsp");
            request.getRequestDispatcher(nextPath).forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}