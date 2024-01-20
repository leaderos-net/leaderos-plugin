package net.leaderos.shared.model.request.impl.discord;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class DiscordSyncRequest extends PostRequest {
    public DiscordSyncRequest(String username) throws IOException {
        super("integrations/discord/sync", new HashMap<String, String>() {{
            put("user", username);
        }});
    }
}
