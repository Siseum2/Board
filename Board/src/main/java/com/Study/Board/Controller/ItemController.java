package com.Study.Board.Controller;


import com.Study.Board.Model.ItemAuctionDto;
import com.Study.Board.Model.ItemDto;
import com.Study.Board.Model.ItemPriceDto;
import com.Study.Board.Service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/item")
    public String itemListController(Model model, @RequestParam(required=false) String searchItemText) throws UnsupportedEncodingException {
        if(searchItemText != null && !searchItemText.isEmpty()) {
            List<ItemDto> itemDtoList = itemService.getSearchItemDtoList(searchItemText);
            model.addAttribute("itemDtoList", itemDtoList);
            model.addAttribute("searchItemText", searchItemText);
        }

        return "listItem";
    }

    @GetMapping("/item/price")
    public String itemPriceController(Model model, @RequestParam(required=true) String searchItemId,
                                      @RequestParam(value="page", defaultValue="1") int page)  {

        List<ItemPriceDto> itemPriceDtoList = new ArrayList<>();
        itemService.callSearchSoldItemPriceApi(itemPriceDtoList, searchItemId, "100");
        model.addAttribute("searchItemName", itemPriceDtoList.get(0).getItemName());
        model.addAttribute("itemPriceDtoList", itemPriceDtoList);

        List<ItemAuctionDto> itemAuctionDtoList = new ArrayList<>();
        itemService.callSearchAuctionItemPriceApi(itemAuctionDtoList, searchItemId, "400");

        PageImpl<ItemAuctionDto> itemAuctionDtoPage = itemService.getItemAuctionDtoPage(page, itemAuctionDtoList);
        model.addAttribute("itemAuctionDtoPage", itemAuctionDtoPage);
        model.addAttribute("itemId", searchItemId);

        return "itemPriceChart";
    }

}
