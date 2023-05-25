package com.study.repository;

import com.study.connection.MyConnection;
import com.study.dto.comment.CommentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository {

    private final MyConnection connection = new MyConnection();

    public void save() {
    }

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
