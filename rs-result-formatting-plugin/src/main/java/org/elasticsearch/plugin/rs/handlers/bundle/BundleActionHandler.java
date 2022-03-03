package org.elasticsearch.plugin.rs.handlers.bundle;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.search.RestSearchAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntConsumer;

public class BundleActionHandler implements RestHandler {

    @Override
    public void handleRequest(RestRequest request, RestChannel channel, NodeClient client) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        IntConsumer setSize = (size) -> {
            searchRequest.source().size(size);
        };
        request.withContentOrSourceParamParserOrNull((parser) -> {
            RestSearchAction.parseSearchRequest(searchRequest, request, parser, client.getNamedWriteableRegistry(), setSize);
        });

        client.search(searchRequest, new BundleResponseHandler(channel));
    }

    @Override
    // Declare all the routes here
    public List<Route> routes() {
        return new ArrayList<>(Arrays.asList(
                new Route(RestRequest.Method.GET, "/_search_fhir_bundle"),
                new Route(RestRequest.Method.POST, "/_search_fhir_bundle"),
                new Route(RestRequest.Method.GET, "/{index}/_search_fhir_bundle"),
                new Route(RestRequest.Method.POST, "/{index}/_search_fhir_bundle"),
                new Route(RestRequest.Method.GET, "/{index}/{type}/_search_fhir_bundle"),
                new Route(RestRequest.Method.POST, "/{index}/{type}/_search_fhir_bundle")
        ));
    }
}
