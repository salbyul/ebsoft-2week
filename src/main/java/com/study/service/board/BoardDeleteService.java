package com.study.service.board;

import com.study.util.WebUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class BoardDeleteService implements BoardService{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardDeleteService EXECUTE");

        WebUtil webUtil = new WebUtil();
        Long index = Long.valueOf(request.getParameter("i"));
        try {
            boardRepository.deleteBoardByBoardId(index);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String nextPath = webUtil.assemblePathAfterDelete(request);
        response.sendRedirect(nextPath);
    }
}
