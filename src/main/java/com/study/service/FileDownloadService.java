package com.study.service;

import com.study.repository.FileRepository;
import com.study.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class FileDownloadService implements Service{

    private final FileRepository fileRepository = new FileRepository();

//    TODO 원래 파일 이름으로 변경해야함
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String realName = request.getParameter("name");
        Long index = Long.valueOf(request.getParameter("i"));
        String fileName = null;
        try {
            fileName = fileRepository.findFileNameByRealName(realName, index);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        File file = new File(WebUtil.PATH + "/" + fileName);

        if (file.exists() && file.isFile()) {
            String mimeType = request.getServletContext().getMimeType(file.toString());

            if (mimeType == null) {
                response.setContentType("application/octet-stream");
            }

            String strClient = request.getHeader("user-agent");

            if (strClient.contains("MSIE 5.5")) {
                response.setHeader("Content-Disposition", "fileName=" + fileName + ";");
            } else {
                response.setHeader("Content-Disposition", "attachment; fileName=" + fileName + ";");
            }

            FileInputStream fileInputStream = new FileInputStream(file);

            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];

            int readData = 0;
            while ((readData = (fileInputStream.read(buffer, 0, buffer.length))) != -1) {

                outputStream.write(buffer, 0, readData);
            }

            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
        }
    }
}
