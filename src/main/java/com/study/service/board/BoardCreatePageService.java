package com.study.service.board;

import com.study.util.WebUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class BoardCreatePageService implements BoardService{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardCreatePageService EXECUTE");

        List<String> categories = null;
        WebUtil webUtil = new WebUtil();
        try {
            categories = boardRepository.getCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("categories", categories);

        String nextPath = webUtil.assembleNextPath(request, "create.jsp");
        request.getRequestDispatcher(nextPath).forward(request, response);
    }
}
