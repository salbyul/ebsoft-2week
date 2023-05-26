package com.study.service.board;

import com.oreilly.servlet.MultipartRequest;
import com.study.dto.board.BoardSaveDto;
import com.study.dto.file.FileSaveDto;
import com.study.policy.DistinctPolicy;
import com.study.repository.FileRepository;
import com.study.util.WebUtil;
import com.study.validator.CreateValidator;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class BoardSaveService implements BoardService {

    private final FileRepository fileRepository = new FileRepository();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("BoardSaveService EXECUTE");

        MultipartRequest multipartRequest = new MultipartRequest(request, WebUtil.PATH, WebUtil.MAX_SIZE, "utf-8", new DistinctPolicy());
        WebUtil webUtil = new WebUtil();
        try {
            List<String> categories = boardRepository.getCategories();
            BoardSaveDto boardSaveDto = getBoardSaveDto(multipartRequest, categories);
            Long savedId = boardRepository.save(boardSaveDto);
            if (savedId == null) {
                request.getRequestDispatcher("/").forward(request, response);
                return;
            }
            fileSave(multipartRequest, savedId, "fileOne");
            fileSave(multipartRequest, savedId, "fileTwo");
            fileSave(multipartRequest, savedId, "fileThree");
            String nextPath = webUtil.assembleNextPath(request, "/detail", savedId);
            response.sendRedirect(nextPath);
        } catch (NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * MultipartRequest에서 쿼리스트링 분리해서 BoardSaveDto에 넣고 리턴
     * @param multipartRequest
     * @return
     */
    private BoardSaveDto getBoardSaveDto(MultipartRequest multipartRequest, List<String> categories) throws NoSuchAlgorithmException {
        String category = multipartRequest.getParameter("category");
        String title = multipartRequest.getParameter("title");
        String writer = multipartRequest.getParameter("writer");
        String content = multipartRequest.getParameter("content");
        String password = new WebUtil().encrypt(multipartRequest.getParameter("password"));

        CreateValidator.CreateValidatorBuilder builder = new CreateValidator.CreateValidatorBuilder(categories);
        CreateValidator createValidator = builder.setCategory(category)
                .setContent(content)
                .setPassword(password)
                .setTitle(title)
                .setWriter(writer)
                .build();

        return createValidator.getBoardDto();
    }

    /**
     * 해당 이름의 파일이 존재하면 파일 처리 후 저장
     * @param multipartRequest
     * @param savedId
     * @param fileSequence
     */
    private void fileSave(MultipartRequest multipartRequest, Long savedId, String fileSequence) {
        File file = multipartRequest.getFile(fileSequence);

        if (file != null) {
            try {
                file = multipartRequest.getFile(fileSequence);
                int first = file.getName().indexOf("[");
                int last = file.getName().lastIndexOf("]");
                int dot = file.getName().lastIndexOf(".");
                String realName = file.getName().substring(first + 1, last) + file.getName().substring(dot);
                FileSaveDto fileSaveDto = new FileSaveDto(file.getName(), realName, savedId);
                fileRepository.save(fileSaveDto);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
