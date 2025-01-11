package net.leaderos.shared.model.request.impl.store;

import net.leaderos.shared.model.request.GetRequest;

import java.io.IOException;

public class ListingRequest extends GetRequest {
    /**
     * Request constructor
     *
     * @param username of the user
     * @throws IOException for HttpUrlConnection
     */
    public ListingRequest(String username) throws IOException {
        super("store/listing?username=" + username);
    }
}
