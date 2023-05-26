<%@ page import="java.util.*" %>
<%@ page import="com.study.dto.board.BoardListDto" %>
<%@ page import="com.study.dto.board.BoardSearchDto" %>
<%@ page import="com.study.dto.board.PagingDto" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
<%
    Optional<String> p = Optional.ofNullable(request.getParameter("page"));
    String offset = p.orElse("0");

    Optional<String> startDate = Optional.ofNullable(request.getParameter("start_date"));
    Optional<String> endDate = Optional.ofNullable(request.getParameter("end_date"));
    Optional<String> category = Optional.ofNullable(request.getParameter("category"));
    Optional<String> search = Optional.ofNullable(request.getParameter("search"));

    BoardListDto boardListDto = (BoardListDto) request.getAttribute("boardListDto");
    List<BoardSearchDto> boardSearchDtoList = boardListDto.getBoardSearchDtoList();
    PagingDto pagingDto = boardListDto.getPagingDto();
    List<String> categories = (List<String>) request.getAttribute("categories");
%>

<div class="mx-auto mt-20 w-7/12">

    <%--    검색--%>
    <div class="border mb-5 text-center">
        <form method="get" action="">
            <div class="py-1">등록일
                <input type="date" name="start_date" class="border mx-7" id="startDate" <% if (startDate.isPresent()) {%>
                       value="<%=startDate.get()%>" <% }%>/> ~
                <input type="date" name="end_date" class="border mx-7" id="endDate" <% if (endDate.isPresent()) {%>
                       value="<%=endDate.get()%>" <% }%>/>
                <select name="category" class="border p-1" id="category">
                    <option value="all">전체 카테고리</option>
                    <% for (int i = 0; i < categories.size(); i++) {
                    %>
                    <option value=<%=categories.get(i)%> <% if (category.isPresent() && category.get().equals(categories.get(i))) { %> selected <% } %>><%=categories.get(i)%>
                    </option>
                    <%
                        }%>
                </select>
                <input type="text" placeholder="검색어를 입력하세요. (제목 + 작성자 + 내용)" name="search" class="border pl-2 w-5/12"
                       id="search" <% if (search.isPresent()) { %> value="<%=search.get()%>" <% } %>/>
                <input type="submit" value="검색"
                       class="border rounded-sm bg-gray-100 px-5 duration-300 hover:duration-300 hover:bg-gray-200 hover:cursor-pointer"/>
            </div>
        </form>
    </div>
    <br/>

    <%--    글 목록--%>
    <div>
        <div>총 <%=pagingDto.getTotalRowCount()%>건</div>
        <br/>
        <div>
            <table class="mx-auto text-center w-full">
                <thead>
                <tr class="border-y">
                    <th class="py-1">카테고리</th>
                    <th></th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>조회수</th>
                    <th>등록 일시</th>
                    <th>수정 일시</th>
                </tr>
                </thead>
                <tbody>
                <% for (BoardSearchDto dto : boardSearchDtoList) {%>
                <tr class="border-b">
                    <td class="py-1"><%=dto.getCategory()%>
                    </td>
                    <% if (dto.hasFile()) {%>
                    <td>O</td>
                    <% } else {%>
                    <td></td>
                    <% } %>
                    <td>
                        <%
                            String path = "/detail?i=" + dto.getId() + "&p=" + offset;
                            if (startDate.isPresent()) path += "&start_date=" + startDate.get();
                            if (endDate.isPresent()) path += "&end_date=" + endDate.get();
                            if (category.isPresent()) path += "&category=" + category.get();
                            if (search.isPresent()) path += "&search=" + search.get();
                            %>
                        <a href="<%=path%>"><%=dto.getTitle()%></a>
                    </td>
                    <td><%=dto.getWriter()%>
                    </td>
                    <td><%=dto.getViews()%>
                    </td>
                    <td><%=dto.getCreatedDate().substring(0, 16)%>
                    </td>
                    <% if (dto.getModifiedDate() == null) {%>
                    <td>-</td>
                    <% } else {%>
                    <td><%=dto.getModifiedDate().substring(0, 16)%>
                    </td>
                    <% } %>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <br/>

    <%--    <div class="bg-red-100 text-center my-16 flex w-5/12 mx-auto">--%>
    <%--        <div class="w-content mx-auto">--%>
    <%--        <button class="mx-3"><<</button>--%>
    <%--        <button class="mx-3"><</button>--%>
    <%--        &lt;%&ndash;%>
    <%--            PagingDto pagingDto = boardList.getPagingDto();--%>
    <%--            for (int i = pagingDto.getFirstPage(); i <= pagingDto.getLastPage(); i++) {--%>
    <%--                %>--%>
    <%--        <button class="ml-1"><%=i%></button>--%>
    <%--        &lt;%&ndash;%>
    <%--            }--%>
    <%--        %>--%>
    <%--        <button class="mx-3">></button>--%>
    <%--        <button class="mx-3">>></button>--%>
    <%--        </div>--%>
    <%--    </div>--%>

    <br/>

    <div class="flex justify-end">
        <button type="button" onclick="create()"
                class="px-5 bg-gray-200 rounded-sm duration-300 hover:duration-300 hover:bg-gray-300">등록
        </button>
    </div>
</div>

<script type="text/javascript">
    const create = () => {
        window.location.href = '/create';
    }
</script>

</body>
</html>
