package com.Study.Board.Model;

import com.Study.Board.Model.Enum.SearchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SearchDto {
    private SearchType searchType;
    private String searchText;
}
