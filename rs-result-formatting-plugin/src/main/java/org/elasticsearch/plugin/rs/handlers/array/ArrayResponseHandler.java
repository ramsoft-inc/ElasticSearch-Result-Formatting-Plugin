package org.elasticsearch.plugin.rs.handlers.array;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestResponse;
import org.elasticsearch.rest.action.RestResponseListener;
import org.elasticsearch.search.SearchHit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ArrayResponseHandler extends RestResponseListener<SearchResponse> {

    public ArrayResponseHandler(RestChannel channel) {
        super(channel);
    }

    @Override
    public RestResponse buildResponse(SearchResponse searchResponse) throws Exception {
        RestRequest request = channel.request();
        String uri = request.uri();
        String filterField = null;
        if(uri.contains("filterField")) {
            String[] urlPath = uri.split("=");
            filterField = urlPath.length > 1 ? urlPath[1] : null;
        }

        return buildResponse(searchResponse, channel.newBuilder(), filterField);
    }

    private @NotNull RestResponse buildResponse(SearchResponse response, XContentBuilder builder, String filterField) throws Exception {
        if (response.getHits() == null || response.getHits().getHits() == null || response.getHits().getHits().length == 0) {
            builder.startArray().endArray();
        } else {
            builder.startArray();
            for (SearchHit hit : response.getHits().getHits()) {
                Map sourceMap = hit.getSourceAsMap();

                if(filterField != null && filterField.trim().length() > 0) {
                    Object value = sourceMap.get(filterField);
                    if(value != null) {
                        builder.value(value);
                    } else {
                        builder.map(sourceMap);
                    }
                } else {
                    builder.map(sourceMap);
                }
            }
            builder.endArray();
        }
        return new RestResponse(response.status(), builder);
    }
}
