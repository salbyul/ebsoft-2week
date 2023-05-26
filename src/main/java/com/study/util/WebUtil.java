package com.study.util;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WebUtil {

    private final List<String> parameters;
    public final static String PATH = "/Users/jh/Desktop/Private/study/files";
    public final static int MAX_SIZE = 1024 * 1024 * 10;

    public WebUtil() {
        List<String> parameters = new ArrayList<>();

        parameters.add("i");
        parameters.add("page");
        parameters.add("start_date");
        parameters.add("end_date");
        parameters.add("category");
        parameters.add("search");

        this.parameters = parameters;
    }

    /**
     * HttpServletRequest를 받아서 각종 파라미터값을 붙여준 다음 주소를 만든다.
     * @param request
     * @param nextPath
     * @return
     */
    public String assembleNextPath(HttpServletRequest request, String nextPath) {

        StringBuilder stringBuilder = new StringBuilder(nextPath + "?");

        for (String parameter : parameters) {
            Optional<String> optionalParam = Optional.ofNullable(request.getParameter(parameter));

            if (optionalParam.isPresent()) {
                String param = optionalParam.get();
                stringBuilder.append(parameter).append("=").append(param).append("&");
            }
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    /**
     * HttpServletRequest를 받아서 각종 파라미터값을 붙여준 다음 주소를 만든다.
     * @param request
     * @param nextPath
     * @param boardIndex
     * @return
     */
    public String assembleNextPath(HttpServletRequest request, String nextPath, Long boardIndex) {

        StringBuilder stringBuilder = new StringBuilder(nextPath + "?");
        boolean flag = false;

        for (String parameter : parameters) {
            Optional<String> optionalParam = Optional.ofNullable(request.getParameter(parameter));

            if (optionalParam.isPresent()) {
                flag = true;
                String param = optionalParam.get();
                stringBuilder.append(parameter).append("=").append(param).append("&");
            }
        }

        if (flag) {
            return stringBuilder.append("&i=").append(boardIndex).toString();
        } else {
            return stringBuilder.deleteCharAt(stringBuilder.length() - 1).append("?i=").append(boardIndex).toString();
        }
    }

    /**
     * SHA-256 알고리즘으로 비밀번호를 변경한다.
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String encrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

    public String assemblePathAfterModify(HttpServletRequest request, String nextPath) {

        StringBuilder stringBuilder = new StringBuilder(nextPath + "?");

        for (String parameter : parameters) {
            if (parameter.equals("btn")) continue;
            Optional<String> optionalParam = Optional.ofNullable(request.getParameter(parameter));

            if (optionalParam.isPresent()) {
                String param = optionalParam.get();
                stringBuilder.append(parameter).append("=").append(param).append("&");
            }
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public String assemblePathAfterDelete(HttpServletRequest request) {

        StringBuilder stringBuilder = new StringBuilder("/?");

        for (String parameter : parameters) {
            if (parameter.equals("i")) continue;
            if (parameter.equals("btn")) continue;
            Optional<String> optionalParam = Optional.ofNullable(request.getParameter(parameter));

            if (optionalParam.isPresent()) {
                String param = optionalParam.get();
                stringBuilder.append(parameter).append("=").append(param).append("&");
            }
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

}
