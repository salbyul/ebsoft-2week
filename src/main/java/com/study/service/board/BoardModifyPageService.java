package com.study.service.board;

import com.study.dto.board.BoardModifyDto;
import com.study.repository.FileRepository;
import com.study.util.WebUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class BoardModifyPageService implements BoardService{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardModifyService EXECUTE");

        WebUtil webUtil = new WebUtil();
        String nextPath;
        Long index = Long.valueOf(request.getParameter("i"));
        try {
            BoardModifyDto boardModifyDto = boardRepository.findBoardModifyByBoardId(index);
            FileRepository fileRepository = new FileRepository();
            List<String> files = fileRepository.findFileNamesByBoardId(index);
            boardModifyDto.setFileList(files);
            request.setAttribute("boardModifyDto", boardModifyDto);

            nextPath = webUtil.assembleNextPath(request, "/modify.jsp");
            if (request.getParameter("reason") != null) {
                nextPath += "&reason=" + request.getParameter("reason");
            }
            request.getRequestDispatcher(nextPath).forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
