package com.study.validator;

import com.study.dto.SearchDto;

import java.sql.SQLException;
import java.util.List;

public class IndexValidator {

    private final SearchDto searchDto;
    private final List<String> categories;

    private IndexValidator(IndexValidatorBuilder builder) {
        this.categories = builder.categories;
        this.searchDto = new SearchDto(builder.offset, validateDate(builder.startDate), validateDate(builder.endDate), validateCategory(builder.category), validateSearch(builder.search));
    }

    public SearchDto getSearchDto() {
        return searchDto;
    }

    /**
     * 날짜형식으로 왔는지 검증
     * @param date
     * @return
     */
    private String validateDate(String date) {
        if (date == null || date.equals("")) return null;
        String[] split = date.split("-");

        if (split.length != 3) return null;

        if (Integer.parseInt(split[0]) < 0 || Integer.parseInt(split[0]) > 9999) return null;
        if (Integer.parseInt(split[1]) < 1 || Integer.parseInt(split[1]) > 12) return null;
        if (Integer.parseInt(split[2]) < 1 || Integer.parseInt(split[2]) > 31) return null;
        return date;
    }

    /**
     * 카테고리가 알맞게 들어왔는지 검증
     * @param category
     * @return
     */
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

    /**
     * 검색어 존재 검증
     * @param search
     * @return
     */
    private String validateSearch(String search) {
        if (search == null || search.equals("")) return null;
        return search;
    }

    public static class IndexValidatorBuilder {
        private int offset;
        private String startDate;
        private String endDate;
        private String category;
        private String search;
        private final List<String> categories;

        public IndexValidatorBuilder(List<String> categories) {
            this.categories = categories;
        }

        public IndexValidatorBuilder setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public IndexValidatorBuilder setStartDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public IndexValidatorBuilder setEndDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public IndexValidatorBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public IndexValidatorBuilder setSearch(String search) {
            this.search = search;
            return this;
        }

        public IndexValidator build() throws SQLException {
            return new IndexValidator(this);
        }
    }
}
