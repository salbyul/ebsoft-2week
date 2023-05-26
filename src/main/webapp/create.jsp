<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>

<%
    List<String> categories = (List<String>) request.getAttribute("categories");
%>

<div class="mt-20 w-7/12 mx-auto">

    <div>
        <%--        multipart/form-data로 변경해야 함--%>
        <form method="post" action="/save" onsubmit="return verify()" enctype="multipart/form-data">

            <%--        카테고리--%>
            <div class="flex border-y">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    카테고리
                </div>
                <div class="py-1 w-9/12">
                    <select name="category" class="border ml-3 w-44" id="category">
                        <option value="null">카테고리 선택</option>
                        <% for (int i = 0; i < categories.size(); i++) {
                        %>
                        <option value=<%=categories.get(i)%>><%=categories.get(i)%>
                        </option>
                        <%
                            }%>
                    </select>
                </div>
            </div>

            <%--    작성자--%>
            <div class="flex border-b">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    작성자
                </div>
                <div class="py-1 w-9/12">
                    <% if (request.getParameter("writer") == null) {%>
                    <input type="text" name="writer" class="border pl-1 ml-3 w-44" id="writer"/>
                    <% } else { %>
                    <input type="text" name="writer" class="border pl-1 ml-3 w-44" id="writer"
                           value="<%=request.getParameter("writer")%>"/>
                    <% } %>
                </div>
            </div>

            <%--    비밀번호--%>
            <div class="flex border-b">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    비밀번호
                </div>
                <div class="py-1 w-9/12">
                    <input type="password" name="password" placeholder="비밀번호" class="border pl-1 ml-3 w-44"
                           id="password"/>
                    <input type="password" placeholder="비밀번호 확인" class="border pl-1 ml-3 w-44" id="passwordVerify"/>
                </div>
            </div>

            <%--    제목--%>
            <div class="flex border-b">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    제목
                </div>
                <div class="py-1 w-9/12">
                    <% if (request.getParameter("title") == null) {%>
                    <input type="text" name="title" class="border pl-1 ml-3 w-full" id="title"/>
                    <% } else { %>
                    <input type="text" name="title" class="border pl-1 ml-3 w-full" id="title"
                           value="<%=request.getParameter("title")%>"/>
                    <% } %>
                </div>
            </div>

            <%--                내용--%>
            <div class="flex border-b">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    <span class="align-middle">
                    내용
                    </span>
                </div>
                <div class="py-1 w-9/12">
                    <% if (request.getParameter("content") == null) {%>
                    <textarea class="resize-none w-full border pl-1 ml-3 h-32" name="content" id="content"></textarea>
                    <% } else { %>
                    <textarea class="resize-none w-full border pl-1 ml-3 h-32" name="content"
                              id="content"><%=request.getParameter("content")%></textarea>
                    <% } %>
                </div>
            </div>

            <%--                파일 첨부--%>
            <div class="flex border-b">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    <span class="align-middle">
                    파일 첨부
                    </span>
                </div>
                <div class="py-1 w-9/12">
                    <div class="my-1">
                        <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled id="fileOneName"/>
                        <label for="fileOne">
                            <span class="border rounded-sm px-2">파일 찾기</span>
                        </label>
                        <input type="file" class="pl-1 ml-3 w-full" id="fileOne" name="fileOne" hidden
                               onchange="onFileChange(1)"/>
                    </div>
                    <div class="my-1">
                        <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled id="fileTwoName"/>
                        <label for="fileTwo">
                            <span class="border rounded-sm px-2">파일 찾기</span>
                        </label>
                        <input type="file" class="pl-1 ml-3 w-full" id="fileTwo" name="fileTwo" hidden
                               onchange="onFileChange(2)"/>
                    </div>
                    <div class="my-1">
                        <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled id="fileThreeName"/>
                        <label for="fileThree">
                            <span class="border rounded-sm px-2">파일 찾기</span>
                        </label>
                        <input type="file" class="pl-1 ml-3 w-full" id="fileThree" name="fileThree" hidden
                               onchange="onFileChange(3)"/>
                    </div>
                </div>
            </div>
    </div>

    <br/>

    <div class="flex justify-between">
        <button onclick="index()" class="px-5 rounded-sm border">취소</button>
        <input type="submit" value="저장" class="px-5 rounded-sm border bg-gray-100 hover:cursor-pointer"/>
    </div>
    </form>

</div>
<script type="text/javascript">
    const index = () => {
        window.history.back();
    }

    const verify = () => {
        if (!verifyCategory()) return false;
        if (!verifyWriter()) return false;
        if (!verifyPassword()) return false;
        if (!verifyTitle()) return false;
        if (!verifyContent()) return false;
        return true;
    }

    const verifyCategory = () => {
        const category = document.getElementById("category");
        if (category.value === 'null') {
            alert("카테고리를 선택해주세요.")
            return false;
        }
        return true;
    }

    const verifyWriter = () => {
        const writer = document.getElementById("writer");
        if (writer.value === null || writer.value === undefined || writer.value.length < 3 || writer.value.length > 4) {
            alert("작성자는 3글자 이상, 5글자 미만이어야 합니다.");
            return false;
        }
        return true;
    }

    const verifyPassword = () => {
        const password = document.getElementById("password");
        const passwordVerify = document.getElementById("passwordVerify");
        if (password.value === null || password.value === undefined || password.value.length < 4 || password.value.length > 15) {
            alert("비밀번호는 4글자 이상, 16글자 미만이어야 합니다.");
            return false;
        }
        if (password.value !== passwordVerify.value) {
            alert("비밀번호가 같지 않습니다.");
            return false;
        }
        return true;
    }

    const verifyTitle = () => {
        const title = document.getElementById("title");
        if (title.value === null || title.value === undefined || title.value.length < 4 || title.value.length > 99) {
            alert("제목은 4글자 이상, 100글자 미만이어야 합니다.");
            return false;
        }
        return true;
    }

    const verifyContent = () => {
        const content = document.getElementById("content");
        if (content.value === null || content.value === undefined || content.value.length < 4 || content.value.length > 1999) {
            alert("내용은 4글자 이상, 2000글자 미만이어야 합니다.");
            return false;
        }
        return true;
    }

    const onFileChange = (index) => {
        if (index === 1) {
            const fileInput = document.getElementById("fileOneName");
            const fileOne = document.getElementById("fileOne");
            fileInput.value = fileOne.files[0].name;
        } else if (index === 2) {
            const fileInput = document.getElementById("fileTwoName");
            const fileTwo = document.getElementById("fileTwo");
            fileInput.value = fileTwo.files[0].name;
        } else if (index === 3) {
            const fileInput = document.getElementById("fileThreeName");
            const fileThree = document.getElementById("fileThree");
            fileInput.value = fileThree.files[0].name;
        }
    }
</script>

</body>
</html>
