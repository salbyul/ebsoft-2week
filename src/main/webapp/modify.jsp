<%@ page import="com.study.dto.board.BoardModifyDto" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Modify</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>

<%
    BoardModifyDto boardModifyDto = (BoardModifyDto) request.getAttribute("boardModifyDto");
%>

<div class="mt-20 w-7/12 mx-auto">

    <div>
        <form method="post" action="" onsubmit="return verify()" enctype="multipart/form-data" id="resultForm">

            <%--        카테고리--%>
            <div class="flex border-y">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    카테고리
                </div>
                <div class="py-1 w-9/12 pl-1">
                    <%=boardModifyDto.getCategory()%>
                </div>
            </div>

            <%--                등록 일시--%>
            <div class="flex border-y">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    등록 일시
                </div>
                <div class="py-1 w-9/12 pl-1">
                    <%=boardModifyDto.getCreatedDate().substring(0, 16)%>
                </div>
            </div>

            <%--                수정 일시--%>
            <div class="flex border-y">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    수정 일시
                </div>
                <div class="py-1 w-9/12 pl-1">
                    <% if (boardModifyDto.getModifiedDate() == null) {
                        out.println("-");
                    } else {
                        out.println(boardModifyDto.getModifiedDate().substring(0, 16));
                    }%>
                </div>
            </div>

            <%--                조회수--%>
            <div class="flex border-y">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    조회수
                </div>
                <div class="py-1 w-9/12 pl-1">
                    <%=boardModifyDto.getViews()%>
                </div>
            </div>

            <%--    작성자--%>
            <div class="flex border-b">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    작성자
                </div>
                <div class="py-1 w-9/12">
                    <input type="text" name="writer" class="border pl-1 ml-3 w-44" id="writer"
                           value="<%=boardModifyDto.getWriter()%>"/>
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
                </div>
            </div>

            <%--    제목--%>
            <div class="flex border-b">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    제목
                </div>
                <div class="py-1 w-9/12">
                    <input type="text" name="title" class="border pl-1 ml-3 w-full" id="title"
                           value="<%=boardModifyDto.getTitle()%>"/>
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
                    <textarea class="resize-none w-full border pl-1 ml-3 h-32" name="content"
                              id="content"><%=boardModifyDto.getContent()%></textarea>
                </div>
            </div>

            <%--                파일 첨부--%>
            <div class="flex border-b">
                <div class="bg-gray-100 w-2/12 py-1 pl-2">
                    <span class="align-middle">
                    파일 첨부
                    </span>
                </div>
                <div class="py-1 w-9/12" id="parentDiv">
                    <%
                        int fileIndex = 0;
                        int count = 0;
                        for (String s : boardModifyDto.getFileList()) {
                            count++; %>
                    <div class="my-1" id="fileDiv<%=count%>">
                        <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled id="oldFile<%=count%>"
                               value="<%=s%>" name="oldFile<%=count%>"/>
                        <a href="/download?name=<%=s%>&i=<%=request.getParameter("i")%>"
                           class="px-3 py-1 border">Download</a>
                        <button class="px-2 py-1 border" id="fileBtn<%=count%>" onclick="deleteFile(<%=count%>)">X
                        </button>
                    </div>
                    <%
                        }
                    %>
                    <%
                        for (int i = count; i < 3; i++) {
                            fileIndex++;
                    %>
                    <div class="my-1">
                        <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled id="file<%=fileIndex%>"/>

                        <label for="fileInput<%=fileIndex%>">
                            <span class="border rounded-sm px-2">파일 찾기</span>
                        </label>

                        <input type="file" class="pl-1 ml-3 w-full" id="fileInput<%=fileIndex%>"
                               name="file<%=fileIndex%>"
                               hidden
                               onchange="onFileChange(<%=fileIndex%>)"/>
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
    </div>

    <br/>

    <div class="flex justify-between">
        <button onclick="window.location.href = assembleParameterWithIndex('/detail')" type="button"
                class="px-5 rounded-sm border">취소
        </button>
        <input type="submit" value="저장" class="px-5 rounded-sm border bg-gray-100 hover:cursor-pointer"/>
    </div>
    </form>

</div>

<script type="text/javascript">
    let count;
    let fileIndex;
    window.onload = () => {
        count = <%=count%> +1;
        fileIndex = <%=fileIndex%> +1;
        const resultForm = document.getElementById("resultForm");
        resultForm.action = assembleParameterWithIndex("/board/modify");
        const urlSearchParams = new URLSearchParams(location.search);
        if (urlSearchParams.has("reason") && urlSearchParams.get("reason") === 'password') {
            alert("비밀번호가 틀렸습니다.")
        }
    }

    const verify = () => {
        for (let i = 1; i <= 3; i++) {
            const input = document.getElementById("oldFile" + i);
            console.log(input);
            if (input !== null) {
                input.disabled = false;
            }
        }
        if (!verifyWriter()) return false;
        if (!verifyTitle()) return false;
        if (!verifyContent()) return false;
        if (!verifyPassword()) return false;
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

    const onFileChange = (index) => {
        console.log(index)
        const input = document.getElementById("file" + index);
        const fileInput = document.getElementById("fileInput" + index);
        console.log(input);
        console.log(fileInput);
        input.value = fileInput.files[0].name;
    }

    const deleteFile = (count) => {
        const div = document.getElementById('fileDiv' + count);
        div.remove();
        createFileInput();
    }

    const createFileInput = () => {
        const div = document.createElement("div");
        const nameInput = document.createElement("input");
        const label = document.createElement("label");
        const span = document.createElement("span");
        const fileInput = document.createElement("input");
        const index = fileIndex;

        div.className = 'my-1';

        nameInput.className = 'border pl-1 ml-3 w-5/12 bg-white';
        nameInput.disabled = true;
        nameInput.type = 'text';
        nameInput.id = 'file' + index;


        label.htmlFor = 'fileInput' + index;

        span.className = 'border rounded-sm px-2';
        span.innerHTML = '파일 찾기';


        fileInput.className = 'pl-1 ml-3 w-full';
        fileInput.type = 'file';
        fileInput.id = 'fileInput' + index;
        fileInput.name = 'file' + index;
        fileInput.hidden = true;
        fileInput.onchange = () => onFileChange(index);
        console.log("new index: " + index);

        label.insertBefore(span, null);
        div.insertBefore(nameInput, null);
        div.insertBefore(label, null);
        div.insertBefore(fileInput, null);
        const parentDiv = document.getElementById('parentDiv');
        parentDiv.insertBefore(div, null);
        fileIndex++;
    }
</script>

</body>
</html>
