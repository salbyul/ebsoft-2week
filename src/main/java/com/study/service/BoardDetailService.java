package com.study.service;

import com.study.dto.board.BoardDetailDto;
import com.study.repository.CommentRepository;
import com.study.repository.FileRepository;
import com.study.util.WebUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BoardDetailService implements BoardService{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardDetailService EXECUTE");

        Optional<String> optionalIndex = Optional.ofNullable(request.getParameter("i"));
        WebUtil webUtil = new WebUtil();
        String nextPath;
        BoardDetailDto boardDetailDto = null;
        CommentRepository commentRepository = new CommentRepository();
        FileRepository fileRepository = new FileRepository();

        long index = Long.parseLong(optionalIndex.orElse("0"));


        if (index == 0) {
            nextPath = webUtil.assembleNextPath(request, "/");
            request.getRequestDispatcher(nextPath).forward(request, response);
        } else {
            nextPath = webUtil.assembleNextPath(request, "detail.jsp");
        }

        try {
            boardDetailDto = boardRepository.findDetailByIndex(index);
            boardDetailDto.setComments(commentRepository.findAllByBoardId(index));
            List<String> fileNames = fileRepository.findFileNamesByBoardId(index);
            boardDetailDto.setFileList(fileNames);
            boardRepository.updateViews(boardDetailDto.getViews(), index);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("boardDetailDto", boardDetailDto);

        request.getRequestDispatcher(nextPath).forward(request, response);
    }
}
