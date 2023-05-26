package com.study.service;

import com.study.dto.comment.CommentSaveDto;
import com.study.repository.CommentRepository;
import com.study.util.WebUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class CommentSaveService implements Service {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("CommentSaveService EXECUTE");

        WebUtil webUtil = new WebUtil();
        String nextPath;
        Long index;
        Optional<String> optionalIndex = Optional.ofNullable(request.getParameter("i"));
        Optional<String> optionalWriter = Optional.ofNullable(request.getParameter("writer"));
        Optional<String> optionalContent = Optional.ofNullable(request.getParameter("content"));

        if (optionalIndex.isEmpty()) {
            nextPath = webUtil.assembleNextPath(request, "404");
            request.getRequestDispatcher(nextPath).forward(request, response);
        }

        index = Long.valueOf(optionalIndex.get());

        if (optionalWriter.isEmpty() || optionalContent.isEmpty()) {
            nextPath = webUtil.assembleNextPath(request, "detail", index);
            request.getRequestDispatcher(nextPath).forward(request, response);
        }

        CommentSaveDto commentSaveDto = new CommentSaveDto(optionalWriter.get(), optionalContent.get(), index);
        CommentRepository commentRepository = new CommentRepository();
        try {
            commentRepository.save(commentSaveDto);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        nextPath = webUtil.assembleNextPath(request, "/detail", index);
        request.getRequestDispatcher(nextPath).forward(request, response);
    }
}
