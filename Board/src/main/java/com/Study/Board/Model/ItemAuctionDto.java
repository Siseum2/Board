package com.Study.Board.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemAuctionDto {
    private LocalDateTime registrationDate;
    private String itemId;
    private String itemName;
    private String itemImageUrl;
    private Long itemCount;
    private Long itemPrice;
    private Long itemUnitPrice;
}
