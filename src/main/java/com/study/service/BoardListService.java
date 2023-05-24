package com.study.service;

import com.study.dto.SearchDto;
import com.study.dto.board.BoardListDto;
import com.study.dto.board.BoardSearchDto;
import com.study.dto.board.PagingDto;
import com.study.repository.BoardRepository;
import com.study.validator.IndexValidator;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BoardListService implements Service {

    private final BoardRepository boardRepository = new BoardRepository();

//    TODO 코드 깨끗하게!
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardListService EXECUTE");
        IndexValidator indexValidator = null;
        BoardListDto boardListDto = new BoardListDto();
        List<String> categories = new ArrayList<>();

        Optional<String> offset = Optional.ofNullable(request.getParameter("offset"));
        Optional<String> startDate = Optional.ofNullable(request.getParameter("startDate"));
        Optional<String> endDate = Optional.ofNullable(request.getParameter("endDate"));
        Optional<String> category = Optional.ofNullable(request.getParameter("category"));
        Optional<String> search = Optional.ofNullable(request.getParameter("search"));

        try {
        categories = boardRepository.getCategories();
        IndexValidator.IndexValidatorBuilder builder = new IndexValidator.IndexValidatorBuilder(categories);
            indexValidator = builder.setOffset(Integer.parseInt(offset.orElse("0")))
                    .setStartDate(startDate.orElse(""))
                    .setEndDate(endDate.orElse(""))
                    .setCategory(category.orElse(""))
                    .setSearch(search.orElse(""))
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SearchDto searchDto = indexValidator.getSearchDto();
        request.setAttribute("categories", categories);

        try {
            List<BoardSearchDto> boardList = boardRepository.findBoardPaging(searchDto, 10);
            Integer count = boardRepository.findCountBySearchDto(searchDto);
            boardListDto.setBoardSearchDtoList(boardList);
            boardListDto.setPagingDto(new PagingDto(Integer.parseInt(Optional.ofNullable(request.getParameter("page")).orElse("0")), count));
            request.setAttribute("boardListDto", boardListDto);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
