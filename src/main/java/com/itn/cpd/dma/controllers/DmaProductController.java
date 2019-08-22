package com.itn.cpd.dma.controllers;

import com.google.gson.Gson;
import com.itn.cpd.dma.entities.ProductMasterDMA;
import com.itn.cpd.dma.services.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DmaProductController {
    private static final byte MUSTS = 0;
    private static final byte SHOULDS = 1;
    private static final byte NOTS = 2;
    
    private static final String MATCH_QUERY = "match";
    private static final String PREFIX_QUERY = "prefix";
    private static final String REGEXP_QUERY = "regexp";

    private Gson gson=new Gson();
    @Autowired
    SearchService searchService;

    @GetMapping("/demo")
    public String home(){
        return "Hello World!";
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/demo/match/")
    public ResponseEntity<List<ProductMasterDMA>> matchItems(@RequestParam(name = "musts", required = false) String musts,
                                                @RequestParam(name = "shoulds", required = false) String shoulds,
                                                @RequestParam(name = "nots", required = false) String nots,
                                                @RequestParam(name = "regex", required = false) String regex,
                                                @RequestParam(name = "indexName") String indexName,
                                                @RequestParam(name = "explain", defaultValue = "false") boolean explain) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();

        if (musts!=null || shoulds!=null || nots!=null) {
            if (musts != null) loadBooleanQuery(booleanQuery, musts, MUSTS);
            if (shoulds != null) loadBooleanQuery(booleanQuery, shoulds, SHOULDS);
            if (nots != null) loadBooleanQuery(booleanQuery, nots, NOTS);

            sourceBuilder.query(booleanQuery).explain(explain);
        }
        else {
            throw new Error("The parameters can't be empty.");
        }
        SearchResponse searchResponse = searchService.searchItems(sourceBuilder, indexName);
        List<String> searchItems= Arrays.stream(searchResponse.getHits().getHits()).map(SearchHit::getSourceAsString).collect(Collectors.toList());
        List<ProductMasterDMA> productList = searchItems.stream().map(item -> gson.fromJson(item, ProductMasterDMA.class)).collect(Collectors.toList());
        return (new ResponseEntity<>(productList, HttpStatus.OK));
    }

    private void loadBooleanQuery(BoolQueryBuilder booleanQuery, String including, int type) {
        List<String[]> values;
        if (including.trim().length()==0) return;
        switch(type) {
            case MUSTS: values = getValues(including);
                for (String[] value : values) {
                    String[] subValue = value[0].split(":");
                    switch (subValue[1]) {
                        case (MATCH_QUERY):
                            booleanQuery.must(QueryBuilders.matchQuery(subValue[0], value[1]));
                            break;
                        case (PREFIX_QUERY):
                            booleanQuery.must(QueryBuilders.prefixQuery(subValue[0], value[1]));
                            break;
                        case (REGEXP_QUERY):
                            booleanQuery.must(QueryBuilders.regexpQuery(subValue[0], value[1]));
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + subValue[0]);
                    }
                }
                break;
            case SHOULDS: values = getValues(including);
                for (String[] value : values) {
                    String[] subValue = value[0].split(":");
                    switch (subValue[1]) {
                        case (MATCH_QUERY):
                            booleanQuery.should(QueryBuilders.matchQuery(subValue[0], value[1]));
                            break;
                        case (PREFIX_QUERY):
                            booleanQuery.should(QueryBuilders.prefixQuery(subValue[0], value[1]));
                            break;
                        case (REGEXP_QUERY):
                            booleanQuery.should(QueryBuilders.regexpQuery(subValue[0], value[1]));
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + subValue[0]);
                    }
                }
                break;
            case NOTS: values = getValues(including);
                for (String[] value : values) {
                    String[] subValue = value[0].split(":");
                    switch (subValue[1]) {
                        case (MATCH_QUERY):
                            booleanQuery.mustNot(QueryBuilders.matchQuery(subValue[0], value[1]));
                            break;
                        case (PREFIX_QUERY):
                            booleanQuery.mustNot(QueryBuilders.prefixQuery(subValue[0], value[1]));
                            break;
                        case (REGEXP_QUERY):
                            booleanQuery.mustNot(QueryBuilders.regexpQuery(subValue[0], value[1]));
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + subValue[0]);
                    }
                }
                break;
            default:
        }
    }

    private List<String[]> getValues(String including) {
        List<String[]> results = new ArrayList<>();
        String[] sets = including.split(",");
        for (String s : sets) {
            String[] set = s.split("=");
            results.add(set);
        }
        return results;
    }
}
