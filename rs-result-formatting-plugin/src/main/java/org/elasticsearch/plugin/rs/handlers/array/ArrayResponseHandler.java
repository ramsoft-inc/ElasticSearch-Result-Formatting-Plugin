package org.elasticsearch.plugin.rs.handlers.array;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestResponse;
import org.elasticsearch.rest.action.RestResponseListener;
import org.elasticsearch.search.SearchHit;
import org.jetbrains.annotations.NotNull;

public class ArrayResponseHandler extends RestResponseListener<SearchResponse> {

    public ArrayResponseHandler(RestChannel channel) {
        super(channel);
    }

    @Override
    public RestResponse buildResponse(SearchResponse searchResponse) throws Exception {
        return buildResponse(searchResponse, channel.newBuilder());
    }

    private @NotNull RestResponse buildResponse(SearchResponse response, XContentBuilder builder) throws Exception {
        if (response.getHits() == null || response.getHits().getHits() == null || response.getHits().getHits().length == 0) {
            builder.startArray().endArray();
        } else {
            builder.startArray();
            for (SearchHit hit : response.getHits().getHits()) {
                builder.map(hit.getSourceAsMap());
            }
            builder.endArray();
        }
        return new BytesRestResponse(response.status(), builder);
    }
}
