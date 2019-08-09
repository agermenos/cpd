package com.itn.cpd.dma.controllers;

import com.itn.cpd.dma.services.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
    @Autowired
    SearchService searchService;

    @RequestMapping("/demo")
    public String home(){
        return "Hello World!";
    }

    @RequestMapping("/demo/match/")
    public ResponseEntity<List<String>> matchItems(@RequestParam(name = "musts", required = false) String musts,
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
            String[] regexArray = regex.split("=");
            RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery(regexArray[0], regexArray[1]);
            sourceBuilder.query(regexpQueryBuilder).explain(explain);
        }
        SearchResponse searchResponse = searchService.searchItems(sourceBuilder, indexName);
        List<String> searchItems= Arrays.stream(searchResponse.getHits().getHits()).map(query -> query.getSourceAsString()).collect(Collectors.toList());
        return (new ResponseEntity<>(searchItems, HttpStatus.OK));
    }

    private void loadBooleanQuery(BoolQueryBuilder booleanQuery, String including, int type) {
        List<String[]> values;
        if (including.trim().length()==0) return;
        switch(type) {
            case MUSTS: values = getValues(including);
                for (String[] value : values) {
                    booleanQuery.must(QueryBuilders.matchQuery(value[0], value[1]));
                }
                break;
            case SHOULDS: values = getValues(including);
                for (String[] value : values) {
                    booleanQuery.should(QueryBuilders.fuzzyQuery(value[0], value[1]));
                }
                break;
            case NOTS: values = getValues(including);
                for (String[] value : values) {
                    booleanQuery.mustNot(QueryBuilders.fuzzyQuery(value[0], value[1]));
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
