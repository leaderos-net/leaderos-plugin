package net.leaderos.shared.model.request.impl.credit;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class SendCreditsRequest extends PostRequest {
    public SendCreditsRequest(String senderUsername, String targetUsername, double amount) throws IOException {
        super("credits/send", new HashMap<String, String>() {{
            put("sender_username", senderUsername);
            put("target_username", targetUsername);
            put("amount", String.valueOf(amount));
        }});
    }
}
