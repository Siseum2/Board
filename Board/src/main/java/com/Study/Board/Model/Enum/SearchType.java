package com.Study.Board.Model.Enum;

import lombok.Getter;


@Getter
public enum SearchType {
    SUBJECT("SUBJECT"),
    SUBJECT_CONTENT("SUBJECT_CONTENT"),
    COMMENT("COMMENT");

    SearchType(String searchType) {
        this.type = searchType;
    }

    private String type;

    public static SearchType ofType(String type) {
        if(type == null) {
            throw new IllegalArgumentException();
        }

        for(SearchType searchType : SearchType.values()) {
            if(searchType.type.equals(type)) {
                return searchType;
            }
        }

        throw new IllegalArgumentException("일치하는 검색 타입이 없습니다.");
    }

}
