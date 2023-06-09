package com.study.servlet;

import com.study.service.*;
import com.study.service.board.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebServlet("/")
public class MyServlet extends HttpServlet {

    private final Map<String, Service> map = new HashMap<>();

    @Override
    public void init() {
        map.put("/", new BoardListService());
        map.put("/detail", new BoardDetailService());
        map.put("/create", new BoardCreatePageService());
        map.put("/save", new BoardSaveService());
        map.put("/download", new FileDownloadService());
        map.put("/comment/save", new CommentSaveService());
        map.put("/password/check", new CheckPasswordService());
        map.put("/board/modify/page", new BoardModifyPageService());
        map.put("/board/delete", new BoardDeleteService());
        map.put("/board/modify", new BoardModifyService());

        log.info("MyServlet INIT");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        log.info("URI : [{}]", requestURI);

        Service service = map.get(requestURI);
        if (service != null) {
            service.execute(req, resp);
        } else {
            req.getRequestDispatcher("404.jsp").forward(req, resp);
        }
    }

}
