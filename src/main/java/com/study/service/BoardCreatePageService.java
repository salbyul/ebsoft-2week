package com.study.service;

import com.study.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BoardCreatePageService implements BoardService{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> categories = null;
        try {
            categories = boardRepository.getCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("categories", categories);
        WebUtil webUtil = new WebUtil();
        String nextPath = webUtil.assembleNextPath(request, "create.jsp");
        request.getRequestDispatcher(nextPath).forward(request, response);
    }
}
