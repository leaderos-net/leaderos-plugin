package net.leaderos.shared.modules.connect.socket;

import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.util.HttpChannelAuthorizer;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpAuthorizer extends HttpChannelAuthorizer {
    private final String apiKey;

    public CustomHttpAuthorizer(String endpoint, String apiKey) {
        super(endpoint);

        this.apiKey = apiKey;
    }

    @Override
    public String authorize(String channelName, String socketId) throws AuthorizationFailureException {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Api-Key", apiKey);
        headers.put("Accept", "application/json");
        this.setHeaders(headers);

        return super.authorize(channelName, socketId);
    }
}
