package com.study.service.board;

import com.oreilly.servlet.MultipartRequest;
import com.study.dto.board.BoardModifySaveDto;
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
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BoardModifyService implements BoardService{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardModifyService EXECUTE");

        MultipartRequest multipartRequest = new MultipartRequest(request, WebUtil.PATH, WebUtil.MAX_SIZE, "utf-8", new DistinctPolicy());
        Long index = Long.valueOf(multipartRequest.getParameter("i"));
        WebUtil webUtil = new WebUtil();
        String nextPath;

        try {
            String findPassword = boardRepository.findPasswordByBoardId(index);
            String password = webUtil.encrypt(multipartRequest.getParameter("password"));
            if (!password.equals(findPassword)) {
                nextPath = webUtil.assemblePathAfterModify(request, "/board/modify/page");
                request.getRequestDispatcher(nextPath + "&reason=password").forward(request, response);
                return;
            } else {
                BoardModifySaveDto boardModifySaveDto = getBoardModifySaveDto(multipartRequest);
                boardRepository.updateBoard(boardModifySaveDto);
            }

            FileRepository fileRepository = new FileRepository();
            List<String> fileNames = new ArrayList<>();
            List<String> oldFileNames = fileRepository.findFileNamesByBoardId(index);

            for (int i = 1; i <= 3; i++) {
                String parameter = multipartRequest.getParameter("oldFile" + i);
                if (parameter != null) {
                    oldFileNames.remove(parameter);
                }
            }

            multipartRequest.getFileNames().asIterator().forEachRemaining(f -> fileNames.add(String.valueOf(f)));

            for (String fileName : fileNames) {
                File file = multipartRequest.getFile(fileName);
                if (file != null) {
                    fileSave(multipartRequest, index, fileName);
                }
            }

            fileRepository.deleteFiles(index, oldFileNames);
            nextPath = webUtil.assembleNextPath(request, "/detail", index);
            response.sendRedirect(nextPath);
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * MultipartRequest에서 쿼리스트링 분리해서 BoardSaveDto에 넣고 리턴
     * @param multipartRequest
     * @return
     */
    private BoardModifySaveDto getBoardModifySaveDto(MultipartRequest multipartRequest) throws NoSuchAlgorithmException {
        String title = multipartRequest.getParameter("title");
        String writer = multipartRequest.getParameter("writer");
        String content = multipartRequest.getParameter("content");
        Long index = Long.valueOf(multipartRequest.getParameter("i"));

        return new BoardModifySaveDto(index, writer, title, content);
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

    /**
     * 파일의 원래 이름을 가져온다.
     * @param fileName
     * @return
     */
    private String extractFileName(String fileName) {
        int first = fileName.indexOf("[");
        int last = fileName.lastIndexOf("]");
        int dot = fileName.lastIndexOf(".");
        return fileName.substring(first + 1, last) + fileName.substring(dot);
    }
}
