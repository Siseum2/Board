package com.Study.Board.Config;

import com.Study.Board.Model.Enum.SearchType;
import org.springframework.core.convert.converter.Converter;


public class SearchTypeRequestConverter implements Converter<String, SearchType> {
    @Override
    public SearchType convert(String searchType) {
        return SearchType.ofType(searchType);
    }
}
