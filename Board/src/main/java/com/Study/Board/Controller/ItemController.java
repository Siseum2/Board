package com.Study.Board.Controller;


import com.Study.Board.Model.ItemDto;
import com.Study.Board.Model.ItemPriceDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

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

@Controller
public class ItemController {
    private final String BASE_URL = "https://api.neople.co.kr/df";
    private final String API_KEY = "MnXFAMlODJraqY2sD6ln42lCvQazzch3";

    @GetMapping("/item")
    public String itemController(Model model, @RequestParam(required=false) String searchItemText) throws UnsupportedEncodingException {
        if(searchItemText != null && !searchItemText.isEmpty()) {
            List<ItemDto> itemDtoList = getSearchItemDtoList(searchItemText);
            model.addAttribute("itemDtoList", itemDtoList);
            model.addAttribute("searchItemText", searchItemText);
        }

        return "listItem";
    }

    @GetMapping("/item/price")
    public String itemPriceController(Model model, @RequestParam(required=true) String searchItemId)  {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();

        List<ItemPriceDto> itemPriceDtoList = new ArrayList<>();
        callSearchAuctionItemPriceApi(webClient, itemPriceDtoList, searchItemId, "100");
        model.addAttribute("searchItemName", itemPriceDtoList.get(0).getItemName());
        model.addAttribute("itemPriceDtoList", itemPriceDtoList);

        return "itemPriceChart";
    }

    private List<ItemDto> getSearchItemDtoList(String searchItemText) throws UnsupportedEncodingException {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();

        String searchItemTextEncodingUrl = URLEncoder.encode(searchItemText, "UTF-8");
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<String> itemIdList = new ArrayList<>();

        callSearchItemApi(webClient, searchItemTextEncodingUrl, itemIdList);
        callSearchAuctionItemApi(webClient, itemDtoList, itemIdList,"1");

        return itemDtoList;
    }

    private void callSearchItemApi(WebClient webClient, String searchItemTextEncodingUrl, List<String> itemIdList) {
        webClient.get()
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

    private void callSearchAuctionItemApi(WebClient webClient, List<ItemDto> itemDtoList, List<String> itemIdList, String limit) {
        Flux.fromIterable(itemIdList)
                .flatMap(itemId -> webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/auction-sold")
                                .queryParam("itemId", itemId)
                                .queryParam("limit", limit)
                                .queryParam("apikey", API_KEY)
                                .build())
                        .retrieve()
                        .bodyToFlux(String.class))
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
                        .doFinally(signalType -> itemDtoList.sort(Comparator.comparing(ItemDto::getItemName)))
                        .blockLast();
    }

    private void callSearchAuctionItemPriceApi(WebClient webClient, List<ItemPriceDto> itemPriceDtoList, String itemId, String limit) {
        String auctionItemPrice = webClient.get()
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
