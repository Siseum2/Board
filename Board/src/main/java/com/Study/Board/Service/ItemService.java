package com.Study.Board.Service;

import com.Study.Board.Model.ItemAuctionDto;
import com.Study.Board.Model.ItemDto;
import com.Study.Board.Model.ItemPriceDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final WebClient dnfWebClient;
    private final String API_KEY = "MnXFAMlODJraqY2sD6ln42lCvQazzch3";

    public List<ItemDto> getSearchItemDtoList(String searchItemText) throws UnsupportedEncodingException {

        String searchItemTextEncodingUrl = URLEncoder.encode(searchItemText, "UTF-8");
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<String> itemIdList = new ArrayList<>();

        callSearchItemApi(searchItemTextEncodingUrl, itemIdList);
        callSearchAuctionItemApi(itemDtoList, itemIdList,"1");

        return itemDtoList;
    }

    public void callSearchItemApi(String searchItemTextEncodingUrl, List<String> itemIdList) {
        dnfWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/items")
                        .queryParam("itemName", searchItemTextEncodingUrl)
                        .queryParam("wordType", "full")
                        .queryParam("limit", "30")
                        .queryParam("apikey", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(searchItemResponse -> {
                    JSONObject searchItemResponseJsonObject = new JSONObject(searchItemResponse);
                    JSONArray searchItemResponseJsonArray = searchItemResponseJsonObject.getJSONArray("rows");
                    for (Object arr : searchItemResponseJsonArray) {
                        JSONObject item = (JSONObject) arr;
                        itemIdList.add((String) item.get("itemId"));
                    }
                }).block();
    }

    private void callSearchAuctionItemApi(List<ItemDto> itemDtoList, List<String> itemIdList, String limit) {
        Flux.fromIterable(itemIdList)
                .flatMap(itemId -> dnfWebClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/auction-sold")
                                .queryParam("itemId", itemId)
                                .queryParam("limit", limit)
                                .queryParam("apikey", API_KEY)
                                .build())
                        .retrieve()
                        .bodyToFlux(String.class))
                .subscribeOn(Schedulers.parallel())
                .doOnNext(searchAuctionItemResponse -> {

                    JSONObject searchAuctionItemResponseJsonObject = new JSONObject(searchAuctionItemResponse);
                    JSONArray searchAuctionItemResponseJsonArray = searchAuctionItemResponseJsonObject.getJSONArray("rows");

                    if(searchAuctionItemResponseJsonArray.length() > 0) {
                        JSONObject auctionItemJson = (JSONObject) searchAuctionItemResponseJsonArray.get(0);
                        String itemImageUrl = getItemImageUrl((String) auctionItemJson.get("itemId"));
                        ItemDto auctionItemDto = ItemDto.builder()
                                .itemId((String) auctionItemJson.get("itemId"))
                                .itemName((String) auctionItemJson.get("itemName"))
                                .itemImageUrl(itemImageUrl)
                                .build();
                        itemDtoList.add(auctionItemDto);
                    }
                })
                .doFinally(resultResponse -> itemDtoList.sort(Comparator.comparing(ItemDto::getItemName)))
                .blockLast();
    }
    public void callSearchSoldItemPriceApi(List<ItemPriceDto> itemPriceDtoList, String itemId, String limit) {
        String auctionItemPrice = dnfWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/auction-sold")
                        .queryParam("itemId", itemId)
                        .queryParam("limit", limit)
                        .queryParam("apikey", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class).block();

        JSONObject auctionItemPriceResponseJsonObject = new JSONObject(auctionItemPrice);
        JSONArray auctionItemPriceResponseJsonArray = auctionItemPriceResponseJsonObject.getJSONArray("rows");

        for (Object arr : auctionItemPriceResponseJsonArray) {
            JSONObject itemPrice = (JSONObject) arr;

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime soldDate = LocalDateTime.parse((String) itemPrice.get("soldDate"), inputFormatter);

            ItemPriceDto itemPriceDto = ItemPriceDto.builder()
                    .soldDate(soldDate)
                    .itemId(itemId)
                    .itemName((String) itemPrice.get("itemName"))
                    .count(Long.valueOf(itemPrice.get("count").toString()))
                    .unitPrice(Long.valueOf(itemPrice.get("unitPrice").toString()))
                    .build();

            itemPriceDtoList.add(itemPriceDto);
        }
    }

    public void callSearchAuctionItemPriceApi(List<ItemAuctionDto> itemAuctionDtoList, String itemId, String limit) {
        String auctionItemPrice = dnfWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/auction")
                        .queryParam("itemId", itemId)
                        .queryParam("limit", limit)
                        .queryParam("apikey", API_KEY)
                        .queryParam("sort","unitPrice:asc")
                        .build())
                .retrieve()
                .bodyToMono(String.class).block();

        JSONObject auctionItemPriceResponseJsonObject = new JSONObject(auctionItemPrice);
        JSONArray auctionItemPriceResponseJsonArray = auctionItemPriceResponseJsonObject.getJSONArray("rows");

        for (Object arr : auctionItemPriceResponseJsonArray) {
            JSONObject itemAuction = (JSONObject) arr;

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime regDate = LocalDateTime.parse((String) itemAuction.get("regDate"), inputFormatter);

            ItemAuctionDto itemAuctionDto = ItemAuctionDto.builder()
                    .registrationDate(regDate)
                    .itemId(itemId)
                    .itemName((String) itemAuction.get("itemName"))
                    .itemImageUrl(getItemImageUrl(itemId))
                    .itemCount(Long.valueOf(itemAuction.get("count").toString()))
                    .itemPrice(Long.valueOf(itemAuction.get("currentPrice").toString()))
                    .itemUnitPrice(Long.valueOf(itemAuction.get("unitPrice").toString()))
                    .build();

            itemAuctionDtoList.add(itemAuctionDto);
        }
    }

    public PageImpl<ItemAuctionDto> getItemAuctionDtoPage(int page, List<ItemAuctionDto> itemAuctionDtoList) {
        Long total = Long.valueOf(itemAuctionDtoList.size());
        int startPage = (page -1)*10;
        int remainPageCount = 10;

        int remainPage = (int) (total - (page -1) * 10);
        if(remainPage < remainPageCount) {
            remainPageCount = remainPage;
        }

        List<ItemAuctionDto> itemAuctionDtoPageList = new ArrayList<>();

        for(int i = 0; i < remainPageCount; i++) {
            itemAuctionDtoPageList.add(itemAuctionDtoList.get(startPage + i));
        }

        Pageable pageable = PageRequest.of(page -1 ,10);
        PageImpl<ItemAuctionDto> itemAuctionDtoPage = new PageImpl<>(itemAuctionDtoPageList, pageable, total);
        return itemAuctionDtoPage;
    }

    private String getItemImageUrl(String itemId) {
        String itemImageUrl = "https://img-api.neople.co.kr/df/items/" + itemId;

        try {
            URL url = new URL(itemImageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode>=400)
                return "";

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

//        webClient.get()
//                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.empty())
//                .bodyToMono(String.class)
//                .flatMap(body -> Mono.just(imageUrl))
//                .defaultIfEmpty("error")
//                .subscribe(body-> itemDtoList.stream().filter(itemDto->itemDto.getItemName().equals(itemId))
//                        .forEach(itemDto -> itemDto.setItemImageUrl(body)));
        // block() 함수를 이용하여 동기화를 2번 처리할 경우 에러가 발생하고 비동기 처리가 복잡하므로
        // HttpURLConnection을 이용하여 itemImageUrl을 가져왔다

        return itemImageUrl;
    }

}
