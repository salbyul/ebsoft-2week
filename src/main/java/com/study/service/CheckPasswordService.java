package com.study.service;

import com.study.service.board.BoardService;
import com.study.util.WebUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Slf4j
public class CheckPasswordService implements BoardService {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("CheckPasswordService EXECUTE");

        WebUtil webUtil = new WebUtil();
        String nextPath;
        String password;
        String index = request.getParameter("i");
        String btn = request.getParameter("btn");

        try {
            password = webUtil.encrypt(request.getParameter("password"));
            String findPassword = boardRepository.findPasswordByBoardId(Long.valueOf(index));
            if (password.equals(findPassword)) {
                if (btn.equals("modify")) {
                    nextPath = webUtil.assembleNextPath(request, "/board/modify/page");
                    request.getRequestDispatcher(nextPath).forward(request, response);
                } else if (btn.equals("delete")) {
                    nextPath = webUtil.assembleNextPath(request, "/board/delete");
                    request.getRequestDispatcher(nextPath).forward(request, response);
                } else {
                    nextPath = webUtil.assembleNextPath(request, "/detail");
                    request.getRequestDispatcher(nextPath).forward(request, response);
                }
            } else {
                nextPath = webUtil.assembleNextPath(request, "/detail");
                request.getRequestDispatcher(nextPath).forward(request, response);
            }
        } catch (NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
        }
    }
}
