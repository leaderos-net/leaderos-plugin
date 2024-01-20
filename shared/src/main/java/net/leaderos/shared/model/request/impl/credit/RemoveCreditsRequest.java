package net.leaderos.shared.model.request.impl.credit;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class RemoveCreditsRequest extends PostRequest {
    public RemoveCreditsRequest(String target, double amount) throws IOException {
        super("credits/" + target + "/remove", new HashMap<String, String>() {{
            put("amount", String.valueOf(amount));
        }});
    }
}
