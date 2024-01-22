package net.leaderos.shared.model.request.impl.donations;

import net.leaderos.shared.model.request.GetRequest;

import java.io.IOException;

public class GetDonationsRequest extends GetRequest {
    /**
     * Request constructor
     *
     * @param type of request
     * @throws IOException for HttpUrlConnection
     */
    public GetDonationsRequest(String type) throws IOException {
        super("store/donations/?type=" + type + "&limit=10");
    }
}
