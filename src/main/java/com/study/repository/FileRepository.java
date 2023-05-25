package com.study.repository;

import com.study.connection.MyConnection;
import com.study.dto.file.FileSaveDto;

import java.sql.*;

public class FileRepository {

    private final MyConnection connection = new MyConnection();

    public Long save(FileSaveDto fileSaveDto) throws SQLException {
        String sql = "insert into file (name, real_name, board_id) values (?, ?, ?)";
        Long generatedKey = 0L;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, fileSaveDto.getName());
            pstmt.setString(2, fileSaveDto.getRealName());
            pstmt.setLong(3, fileSaveDto.getBoardId());

            int i = pstmt.executeUpdate();

            if (i > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedKey = rs.getLong(1);
                }
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
            rs.close();
        }
        return generatedKey;
    }

    /**
     * 게시판이 가지고 있는 파일의 갯수 확인
     * @param boardId
     * @return
     * @throws SQLException
     */
    public int countFiles(Long boardId) throws SQLException {
        String sql = "select count(file_id) from file where board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
            rs.close();
        }
        return result;
    }
}
