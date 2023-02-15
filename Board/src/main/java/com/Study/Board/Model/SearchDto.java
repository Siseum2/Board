package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SearchDto {
    private String searchType;
    private String searchText;
}
