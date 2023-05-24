package com.study.repository;

import com.study.connection.MyConnection;
import com.study.dto.board.BoardSearchDto;
import com.study.dto.SearchDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardRepository {

    private final MyConnection connection = new MyConnection();

//    TODO SQL Injection 대비
    public List<BoardSearchDto> findBoardPaging(SearchDto searchDto, int limit) throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select board_id, name as category, title, writer, views, created_date, modified_date " +
                "from board b, category c " +
                "where b.category_id = c.category_id ";
        if (searchDto.getStartDate() != null) sql = sql + "and b.created_date >= '" + searchDto.getStartDate() + "' ";
        if (searchDto.getEndDate() != null) sql = sql + "and b.created_date <= '" + searchDto.getEndDate() + " 23:59:59' ";
        if (searchDto.getSearch() != null) sql = sql + "and (b.title like '%" + searchDto.getSearch() + "%' or b.writer like '%" + searchDto.getSearch() + "%' or b.content like '%" + searchDto.getSearch() + "%') ";
        if (searchDto.getCategory() != null) sql = sql + "and c.name = '" + searchDto.getCategory() + "' ";

        String orderBy = "order by created_date desc " +
                "limit ? offset ?;";
        sql += orderBy;
        List<BoardSearchDto> list = new ArrayList<>();
        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, searchDto.getOffset() * limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new BoardSearchDto(rs.getLong("board_id"), rs.getString("category"), rs.getString("title"), rs.getString("writer"), rs.getLong("views"), rs.getString("created_date"), rs.getString("modified_date")));
                System.out.println("board_id = " + rs.getLong("board_id"));
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
            rs.close();
        }
        return list;
    }

//    TODO SQL Injection 대비
    public Integer findCountBySearchDto(SearchDto searchDto) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "select count(b.board_id) " +
                "from board b, category c " +
                "where b.category_id = c.category_id ";

        if (searchDto.getStartDate() != null) sql = sql + "and b.created_date >= '" + searchDto.getStartDate() + "' ";
        if (searchDto.getEndDate() != null) sql = sql + "and b.created_date <= '" + searchDto.getEndDate() + " 23:59:59' ";
        if (searchDto.getSearch() != null) sql = sql + "and (b.title like '%" + searchDto.getSearch() + "%' or b.writer like '%" + searchDto.getSearch() + "%' or b.content like '%" + searchDto.getSearch() + "%') ";
        if (searchDto.getCategory() != null) sql = sql + "and c.name = '" + searchDto.getCategory() + "' ";

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

//    TODO SQLException 이거 제거 불가?
    public List<String> getCategories() throws SQLException {
        String sql = "select name from category";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> categories = new ArrayList<>();

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
            rs.close();
        }
        return categories;
    }

}
