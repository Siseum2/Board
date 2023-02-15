package com.Study.Board.Model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    private String itemId;
    private String itemName;
    private String itemImageUrl;
}
