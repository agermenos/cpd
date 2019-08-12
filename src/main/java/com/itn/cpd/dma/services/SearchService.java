package com.itn.cpd.dma.services;

import com.google.gson.Gson;
import com.itn.cpd.dma.entities.ProductMasterDMA;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchService {
    @Autowired
    private RestHighLevelClient client;

    private Gson gson = new Gson();

    public void addIndex(ProductMasterDMA product) throws IOException {

        IndexRequest indexRequest = new IndexRequest("products")
                .id(product.getId().toString()).source(gson.toJson(product), XContentType.JSON);
        indexRequest.opType("create").create(true);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public SearchResponse searchItems(SearchSourceBuilder sourceBuilder, String indices) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indices);

        sourceBuilder.size(100);
        searchRequest.source(sourceBuilder);

        return client.search(searchRequest,RequestOptions.DEFAULT);
    }
}
