package net.leaderos.shared.model.request.impl.credit;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class RemoveCreditsRequest extends PostRequest {
    public RemoveCreditsRequest(String targetUsername, double amount) throws IOException {
        super("credits/remove", new HashMap<String, String>() {{
            put("target_username", String.valueOf(targetUsername));
            put("amount", String.valueOf(amount));
        }});
    }
}
