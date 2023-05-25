package com.study.service;

import com.oreilly.servlet.MultipartRequest;
import com.study.dto.board.BoardSaveDto;
import com.study.dto.file.FileSaveDto;
import com.study.policy.DistinctPolicy;
import com.study.repository.FileRepository;
import com.study.util.WebUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class BoardSaveService implements BoardService{


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardSaveService EXECUTE");

        String PATH = "/Users/jh/Desktop/Private/study/files";
        int MAX_SIZE = 1024 * 1024 * 5;
        MultipartRequest multipartRequest = new MultipartRequest(request, PATH, MAX_SIZE, "utf-8", new DistinctPolicy());
        WebUtil webUtil = new WebUtil();

        BoardSaveDto boardSaveDto = getBoardSaveDto(multipartRequest);
        Long savedId = null;

        try {
            savedId = boardRepository.save(boardSaveDto);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (savedId == null) {
            request.getRequestDispatcher("/").forward(request, response);
        }

        fileSave(multipartRequest, savedId, "fileOne");
        fileSave(multipartRequest, savedId, "fileTwo");
        fileSave(multipartRequest, savedId, "fileThree");


        String nextPath = webUtil.assembleNextPath(request, "/detail", savedId);
        request.getRequestDispatcher(nextPath).forward(request, response);
    }

    /**
     * MultipartRequest에서 쿼리스트링 분리해서 BoardSaveDto에 넣고 리턴
     * @param multipartRequest
     * @return
     */
    private BoardSaveDto getBoardSaveDto(MultipartRequest multipartRequest) {
        String category = multipartRequest.getParameter("category");
        String title = multipartRequest.getParameter("title");
        String writer = multipartRequest.getParameter("writer");
        String content = multipartRequest.getParameter("content");
        String password = multipartRequest.getParameter("password");

        BoardSaveDto boardSaveDto = new BoardSaveDto(category, writer, password, title, content);
        return boardSaveDto;
    }

    /**
     * 해당 이름의 파일이 존재하면 파일 처리 후 저장
     * @param multipartRequest
     * @param savedId
     * @param fileSequence
     */
    private void fileSave(MultipartRequest multipartRequest, Long savedId, String fileSequence) {
        FileRepository fileRepository = new FileRepository();
        if (multipartRequest.getFile(fileSequence) != null) {
            File file = multipartRequest.getFile(fileSequence);

            String realName;
            int first = file.getName().indexOf("[");
            int last = file.getName().lastIndexOf("]");
            int dot = file.getName().lastIndexOf(".");
            realName = file.getName().substring(first + 1, last) + file.getName().substring(dot);

            FileSaveDto fileSaveDto = new FileSaveDto(file.getName(), realName, savedId);

            try {
                fileRepository.save(fileSaveDto);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
