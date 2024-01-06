package net.leaderos.shared.model.request.impl.store;

import net.leaderos.shared.model.request.GetRequest;

import java.io.IOException;

public class ListingRequest extends GetRequest {
    /**
     * Request constructor
     *
     * @param api of request
     * @throws IOException for HttpUrlConnection
     */
    public ListingRequest() throws IOException {
        super("store/listing");
    }
}
