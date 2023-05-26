package com.study.repository;

import com.study.connection.MyConnection;
import com.study.dto.board.*;
import com.study.dto.SearchDto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardRepository {

    private final MyConnection connection = new MyConnection();

    //    TODO SQL Injection 대비
    public List<BoardSearchDto> findBoardPaging(SearchDto searchDto, int limit) throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select b.board_id, c.name as category, title, writer, views, created_date, modified_date, count(file_id) as file_count " +
                "from board b " +
                "left join category c " +
                "on c.category_id = b.category_id " +
                "left join file f " +
                "on f.board_id = b.board_id " +
                "where b.category_id = c.category_id ";
        if (searchDto.getStartDate() != null) sql = sql + "and b.created_date >= '" + searchDto.getStartDate() + "' ";
        if (searchDto.getEndDate() != null)
            sql = sql + "and b.created_date <= '" + searchDto.getEndDate() + " 23:59:59' ";
        if (searchDto.getSearch() != null)
            sql = sql + "and (b.title like '%" + searchDto.getSearch() + "%' or b.writer like '%" + searchDto.getSearch() + "%' or b.content like '%" + searchDto.getSearch() + "%') ";
        if (searchDto.getCategory() != null) sql = sql + "and c.name = '" + searchDto.getCategory() + "' ";

        String groupBy = "group by b.board_id, category, title, writer, views, created_date, modified_date ";
        sql += groupBy;

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
                BoardSearchDto boardSearchDto = new BoardSearchDto(rs.getLong("board_id"), rs.getString("category")
                        , rs.getString("title"), rs.getString("writer")
                        , rs.getLong("views"), rs.getString("created_date")
                        , rs.getString("modified_date"));
                if (rs.getInt("file_count") > 0) {
                    boardSearchDto.setHasFile(true);
                }
                list.add(boardSearchDto);
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

        Connection conn;
        PreparedStatement pstmt;
        ResultSet rs;

        String sql = "select count(b.board_id) " +
                "from board b, category c " +
                "where b.category_id = c.category_id ";

        if (searchDto.getStartDate() != null) sql = sql + "and b.created_date >= '" + searchDto.getStartDate() + "' ";
        if (searchDto.getEndDate() != null)
            sql = sql + "and b.created_date <= '" + searchDto.getEndDate() + " 23:59:59' ";
        if (searchDto.getSearch() != null)
            sql = sql + "and (b.title like '%" + searchDto.getSearch() + "%' or b.writer like '%" + searchDto.getSearch() + "%' or b.content like '%" + searchDto.getSearch() + "%') ";
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

    public BoardDetailDto findDetailByIndex(Long index) throws SQLException {
        String sql = "select board_id, title, content, created_date, modified_date, writer, views, name as category from board, category where board_id = ? and category.category_id = board.category_id";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BoardDetailDto boardDetailDto = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, index);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                long boardId = rs.getLong("board_id");
                String title = rs.getString("title");
                String category = rs.getString("category");
                String content = rs.getString("content");
                String createdDate = rs.getString("created_date");
                String modifiedDate = rs.getString("modified_date");
                String writer = rs.getString("writer");
                int views = rs.getInt("views");
                rs.close();
                boardDetailDto = new BoardDetailDto(boardId, category, title, views, content, createdDate, modifiedDate, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
            rs.close();
        }
        return boardDetailDto;
    }

    public Long save(BoardSaveDto boardDto) throws SQLException {
        String sql = "insert into board (category_id, writer, password, title, content, views, created_date, modified_date) values((select category_id from category where name = ?), ?, ?, ?, ?, 0, ?, null)";
        Long generatedKey = 0L;


        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, boardDto.getCategory());
            pstmt.setString(2, boardDto.getWriter());
            pstmt.setString(3, boardDto.getPassword());
            pstmt.setString(4, boardDto.getTitle());
            pstmt.setString(5, boardDto.getContent());
            pstmt.setString(6, String.valueOf(LocalDateTime.now()));

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

    public void updateViews(int views, Long boardId) throws SQLException {
        String sql = "update board set views = ? + 1 where board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, views);
            pstmt.setLong(2, boardId);
            int i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
        }
    }

    /**
     * board_id로 비밀번호를 찾는다.
     *
     * @param boardId
     * @return
     * @throws SQLException
     */
    public String findPasswordByBoardId(Long boardId) throws SQLException {
        String sql = "select password from board where board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String result = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardId);
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
     * board_id로 board를 찾은 뒤 BoardModifyDto 객체를 리턴한다.
     *
     * @param boardId
     * @return
     */
    public BoardModifyDto findBoardModifyByBoardId(Long boardId) throws SQLException {
        String sql = "select c.name as category, b.writer, b.title, b.content, b.views, b.created_date, b.modified_date " +
                "from board b " +
                "left join category c " +
                "on b.category_id = c.category_id " +
                "where b.board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BoardModifyDto boardModifyDto = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, boardId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                boardModifyDto = new BoardModifyDto(rs.getString("category"), rs.getString("writer"),
                        rs.getString("title"), rs.getString("content"), rs.getInt("views"),
                        rs.getString("created_date"), rs.getString("modified_date"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
            rs.close();
        }
        return boardModifyDto;
    }

    /**
     * board_id를 받아 해당 board를 DB에서 삭제한다.
     * @param boardId
     * @throws SQLException
     */
    public void deleteBoardByBoardId(Long boardId) throws SQLException {
        String sql = "delete from board where board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
        }
    }

    /**
     * BoardModifySaveDto를 받아서 DB에 있는 해당 레코드를 업데이트한다.
     * @param boardModifySaveDto
     */
    public void updateBoard(BoardModifySaveDto boardModifySaveDto) throws SQLException {
        String sql = "update board set writer = ?, title = ?, content = ?, modified_date = ? where board_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, boardModifySaveDto.getWriter());
            pstmt.setString(2, boardModifySaveDto.getTitle());
            pstmt.setString(3, boardModifySaveDto.getContent());
            pstmt.setString(4, String.valueOf(LocalDateTime.now()));
            pstmt.setLong(5, boardModifySaveDto.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            pstmt.close();
        }
    }

}
