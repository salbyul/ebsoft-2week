package com.study.repository;

import com.study.connection.MyConnection;
import com.study.dto.comment.CommentDto;
import com.study.dto.comment.CommentSaveDto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository {

    private final MyConnection connection = new MyConnection();

    public Long save(CommentSaveDto commentSaveDto) throws SQLException {
        String sql = "insert into comment (writer, content, created_date, board_id) values (?, ?, ?, ?)";
        Long generatedKey = 0L;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, commentSaveDto.getWriter());
            pstmt.setString(2, commentSaveDto.getContent());
            pstmt.setString(3, LocalDateTime.now().toString());
            pstmt.setLong(4, commentSaveDto.getBoardId());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getLong(1);
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
     * board_id
     * @param boardId
     * @return
     * @throws SQLException
     */
    public List<CommentDto> findAllByBoardId(Long boardId) throws SQLException {
        String sql = "select comment_id, writer, content, created_date from comment where board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CommentDto> commentDtoList = new ArrayList<>();

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CommentDto commentDto = new CommentDto(rs.getLong("comment_id"), rs.getString("writer"), rs.getString("content"), rs.getString("created_date"));
                commentDtoList.add(commentDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
            rs.close();
        }
        return commentDtoList;
    }
}
