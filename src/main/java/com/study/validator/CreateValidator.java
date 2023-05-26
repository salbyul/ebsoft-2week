package com.study.validator;

import com.study.dto.board.BoardSaveDto;

import java.util.List;

public class CreateValidator {

    private final BoardSaveDto boardDto;
    private final List<String> categories;

    private CreateValidator(CreateValidatorBuilder builder) {
        this.categories = builder.categories;
        this.boardDto = new BoardSaveDto(validateCategory(builder.category), validateWriter(builder.writer), validatePassword(builder.password), validateTitle(builder.title), validateContent(builder.content));
    }

    public BoardSaveDto getBoardDto() {
        return boardDto;
    }

    private String validateCategory(String category) {
        if (category == null || category.equals("")) return null;

        boolean flag = false;
        for (String s : categories) {
            if (category.equals(s)) {
                flag = true;
                break;
            }
        }
        if (flag) return category;
        return null;
    }

    private String validateWriter(String writer) {
        if (writer != null) {
            if (writer.length() < 3 || writer.length() > 4) {
                throw new IllegalArgumentException("writer ERROR!!");
            } else {
                return writer;
            }
        }
        throw new IllegalArgumentException("writer ERROR!!");
    }

    private String validatePassword(String password) {
        return password;
    }

    private String validateTitle(String title) {
        if (title != null) {
            if (title.length() < 4 || title.length() > 99) {
                throw new IllegalArgumentException("title ERROR!!");
            }
            return title;
        }
        throw new IllegalArgumentException("title ERROR!!");
    }

    private String validateContent(String content) {
        if (content != null) {
            if (content.length() < 4 || content.length() > 1999) {
                throw new IllegalArgumentException("content ERROR!!");
            }
            return content;
        }
        throw new IllegalArgumentException("content ERROR!!");
    }

    public static class CreateValidatorBuilder {

        private String category;
        private String writer;
        private String password;
        private String title;
        private String content;
        private final List<String> categories;

        public CreateValidatorBuilder(List<String> categories) {
            this.categories = categories;
        }

        public CreateValidatorBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public CreateValidatorBuilder setWriter(String writer) {
            this.writer = writer;
            return this;
        }

        public CreateValidatorBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public CreateValidatorBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public CreateValidatorBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public CreateValidator build() {
            return new CreateValidator(this);
        }
    }
}
