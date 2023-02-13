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
public class ItemPriceDto {
    private LocalDateTime soldDate;
    private String itemId;
    private String itemName;
    private Long count;
    private Long unitPrice;
}
