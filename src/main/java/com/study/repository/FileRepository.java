package com.study.repository;

import com.study.connection.MyConnection;
import com.study.dto.file.FileSaveDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * board_id로 board에 있는 파일 이름들 리턴
     * @param boardId
     * @return
     * @throws SQLException
     */
    public List<String> findFileNamesByBoardId(Long boardId) throws SQLException {
        String sql = "select real_name from file where board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> fileNames = new ArrayList<>();

        try {
            conn = connection.getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                fileNames.add(rs.getString("real_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
            rs.close();
        }
        return fileNames;
    }

    /**
     * real_name으로 변환된 변환된 파일 이름 리턴
     * @param realName
     * @param boardId
     * @return
     * @throws SQLException
     */
    public String findFileNameByRealName(String realName, Long boardId) throws SQLException {

        String sql = "select name from file where real_name = ? and board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String result = null;

        try {

            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, realName);
            pstmt.setLong(2, boardId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getString(1);
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

    /**
     * 특정 board 레코드와 연결되어 있는 file 레코드들 중에 fileNames에 포함되어 있는 레코드 삭제
     * @param boardId
     * @param fileNames
     */
    public void deleteFiles(Long boardId, List<String> fileNames) throws SQLException {
        for (String fileName : fileNames) {
            String sql = "delete from file where board_id = ? and real_name = ?";

            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                conn = connection.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, boardId);
                pstmt.setString(2, fileName);

                pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.close();
                pstmt.close();
            }
        }
    }

}
