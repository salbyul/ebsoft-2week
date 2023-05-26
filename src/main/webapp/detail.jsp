<%@ page import="com.study.dto.comment.CommentDto" %>
<%@ page import="java.util.Optional" %>
<%@ page import="com.study.dto.board.BoardDetailDto" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Detail</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>

<%
    String index = request.getParameter("i");
    BoardDetailDto detail = (BoardDetailDto) request.getAttribute("boardDetailDto");
%>


<div class="w-7/12 mx-auto mt-20 h-screen">

    <%--  제목--%>
    <div class="border-b-2 pb-3 mb-3">
        <div class="flex justify-between text-sm text-gray-600 mb-5">
            <div>
                <%=detail.getWriter()%>
            </div>
            <div>
                등록일시 <%=detail.getCreatedDate().substring(0, 16)%>&nbsp;&nbsp;&nbsp;&nbsp;수정일시 <% if (detail.getModifiedDate() == null) {
                out.println("-");
            } else {
                out.println(detail.getModifiedDate().substring(0, 16));
            }%>
            </div>
        </div>
        <div class="flex justify-between text-gray-700">
            <div class="flex">
                <div class="mr-4">
                    [<%=detail.getCategory()%>]
                </div>
                <div>
                    <%=detail.getTitle()%>
                </div>
            </div>
            <div>
                조회수: <%=detail.getViews() + 1%>
            </div>
        </div>
    </div>

    <%--  내용--%>
    <div class="mb-3">
        <div class="border pl-1 whitespace-pre-wrap"><%=detail.getContent()%>
        </div>
        <%--    file--%>
        <div>
            <%
                for (String s : detail.getFileList()) {
            %>
            <a href="download?name=<%=s%>&i=<%=index%>"><%=s%>
            </a>
            <br/>
            <%
                }
            %>
        </div>
    </div>

    <%--  댓글--%>
    <div class="border-b pb-2 bg-gray-50 px-1">
        <div>
            <% for (CommentDto c : detail.getComments()) {%>
            <div class="border-b-2 pb-2 mb-2">
                <div class="flex justify-between text-gray-600 text-xs">
                    <div class="pl-3">
                        <%=c.getWriter()%>
                    </div>
                    <div class="pr-3">
                        <%=c.getCreatedDate().substring(0, 16)%>
                    </div>
                </div>
                <div class="pl-3">
                    <%=c.getContent()%>
                </div>
            </div>
            <% } %>
        </div>
        <div>
            <form method="post" id="commentForm" onsubmit="return verifyComment()">
                <div>
                    <div>
                        <input type="text" placeholder="작성자" class="border pl-1 mb-1" name="writer" id="writer"/>
                    </div>
                    <div class="flex">
                        <textarea class="resize-none border h-20 pl-1 w-11/12" name="content" id="content"></textarea>
                        <input type="submit" value="등록" class="border w-1/12 h-20 rounded-sm hover:cursor-pointer"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="text-center mt-2">
        <button class="px-3 py-1 border rounded-sm bg-gray-100" onclick="window.location.href = assembleParameter('/')">
            목록
        </button>
        <button class="px-3 py-1 border rounded-sm" onclick="changeVisibilityPopup('modify')">수정</button>
        <button class="px-3 py-1 border rounded-sm" onclick="changeVisibilityPopup('delete')">삭제</button>
    </div>
</div>

<%--수정--%>
<div class="border fixed z-10 w-3/12 text-center top-1/2 left-1/2 -translate-y-1/2 -translate-x-1/2 shadow-md bg-gray-50 invisible"
     id="modify_popup">
    <form method="post" action="" id="modifiedForm">
        <h1 class="my-10">비밀번호 입력</h1>
        <input type="password" placeholder="비밀번호를 입력해 주세요." class="pl-1 border w-5/12" name="password"/>
        <br/>
        <div class="flex justify-evenly my-10">
            <button type="button" class="bg-gray-100 px-3 py-1" onclick="changeVisibilityPopup('modify')">취소</button>
            <input type="submit" value="확인" class="px-3 py-1 bg-blue-100 hover:cursor-pointer"/>
        </div>
    </form>
</div>

<%--삭제--%>
<div class="border fixed z-10 w-3/12 text-center top-1/2 left-1/2 -translate-y-1/2 -translate-x-1/2 shadow-md bg-gray-50 invisible"
     id="delete_popup">
    <form method="post" action="" id="deletedForm">
        <h1 class="my-10">비밀번호 입력</h1>
        <input type="password" placeholder="비밀번호를 입력해 주세요." class="pl-1 border w-5/12" name="password"/>
        <br/>
        <div class="flex justify-evenly my-10">
            <button type="button" class="bg-gray-100 px-3 py-1" onclick="changeVisibilityPopup('delete')">취소</button>
            <input type="submit" value="확인" class="px-3 py-1 bg-blue-100 hover:cursor-pointer"/>
        </div>
    </form>
</div>
<%
    Optional<String> optionalBtn = Optional.ofNullable(request.getParameter("btn"));
    if (optionalBtn.isPresent()) {
        String btn = optionalBtn.get();
        if (btn.equals("modify")) {
%>
<script>
    const modifyPopup = document.getElementById("modify_popup");
    modifyPopup.style.visibility = 'visible';
    alert("비밀번호가 틀렸습니다.")
</script>
<%
} else if (btn.equals("delete")) { %>
<script>
    const deletePopup = document.getElementById("delete_popup");
    deletePopup.style.visibility = 'visible';
    alert("비밀번호가 틀렸습니다.");
</script>
<%
        }
    }
%>
<script type="text/javascript">
    window.onload = () => {
        const modifiedForm = document.getElementById("modifiedForm");
        modifiedForm.action = assembleParameterWithIndex("password/check") + '&btn=modify';
        const commentSaveForm = document.getElementById("commentForm");
        commentSaveForm.action = assembleParameterWithIndex("comment/save");
        const deletedForm = document.getElementById("deletedForm");
        deletedForm.action = assembleParameterWithIndex("password/check") + '&btn=delete';
    }
    const verifyComment = () => {
        const writer = document.getElementById("writer");
        const content = document.getElementById("content");
        if (writer.value === null || writer.value === undefined || writer.value.length < 3 || writer.value.length > 4) {
            alert("작성자는 3글자 이상, 5글자 미만이어야 합니다.");
            return false;
        }
        if (content.value === null || content.value === undefined || content.value.length === 0) {
            alert("내용을 입력해주세요.")
            return false;
        }
        return true;
    }

    const changeVisibilityPopup = (btn) => {
        const modifyPopup = document.getElementById("modify_popup");
        const deletePopup = document.getElementById("delete_popup");
        if (btn === 'modify') {
            const visibility = modifyPopup.style.visibility;
            console.log(visibility);
            if (visibility === 'visible') {
                modifyPopup.style.visibility = 'hidden';
            } else {
                deletePopup.style.visibility = 'hidden';
                modifyPopup.style.visibility = 'visible';
            }
        } else if (btn === 'delete') {
            const visibility = deletePopup.style.visibility;
            console.log(visibility);
            if (visibility === 'visible') {
                deletePopup.style.visibility = 'hidden';
            } else {
                modifyPopup.style.visibility = 'hidden';
                deletePopup.style.visibility = 'visible';
            }
        }
    }

    const assembleParameter = (path) => {
        const paramGetter = new URLSearchParams(location.search);
        let flag = false;
        if (paramGetter.has('page')) {
            path = path + '?page=' + paramGetter.get('page');
            flag = true;
        }
        if (paramGetter.has('start_date')) {
            if (flag) {
                path = path + '&start_date=' + paramGetter.get('start_date');
            } else {
                path = path + '?start_date=' + paramGetter.get('start_date');
                flag = true;
            }
        }
        if (paramGetter.has('end_date')) {
            if (flag) {
                path = path + '&end_date=' + paramGetter.get('end_date');
            } else {
                path = path + '?end_date=' + paramGetter.get('end_date');
                flag = true;
            }
        }
        if (paramGetter.has('category')) {
            if (flag) {
                path = path + '&category=' + paramGetter.get('category');
            } else {
                path = path + '?category=' + paramGetter.get('category');
                flag = true;
            }
        }
        if (paramGetter.has('search')) {
            if (flag) {
                path = path + '&search=' + paramGetter.get('search');
            } else {
                path = path + '?search=' + paramGetter.get('search');
                flag = true;
            }
        }
        return path;
    }

    const assembleParameterWithIndex = (path) => {
        const paramGetter = new URLSearchParams(location.search);
        let flag = false;
        if (paramGetter.has('page')) {
            path = path + '?page=' + paramGetter.get('page');
            flag = true;
        }
        if (paramGetter.has('start_date')) {
            if (flag) {
                path = path + '&start_date=' + paramGetter.get('start_date');
            } else {
                path = path + '?start_date=' + paramGetter.get('start_date');
                flag = true;
            }
        }
        if (paramGetter.has('end_date')) {
            if (flag) {
                path = path + '&end_date=' + paramGetter.get('end_date');
            } else {
                path = path + '?end_date=' + paramGetter.get('end_date');
                flag = true;
            }
        }
        if (paramGetter.has('category')) {
            if (flag) {
                path = path + '&category=' + paramGetter.get('category');
            } else {
                path = path + '?category=' + paramGetter.get('category');
                flag = true;
            }
        }
        if (paramGetter.has('search')) {
            if (flag) {
                path = path + '&search=' + paramGetter.get('search');
            } else {
                path = path + '?search=' + paramGetter.get('search');
                flag = true;
            }
        }
        if (flag) {
            path = path + '&i=' + paramGetter.get('i');
        } else {
            path = path + '?i=' + paramGetter.get('i');
        }
        return path;
    }
</script>

</body>
</html>
