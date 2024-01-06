package net.leaderos.shared.model.request.impl.credit;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class SetCreditsRequest extends PostRequest {
    public SetCreditsRequest(String target, double amount) throws IOException {
        super("credits/" + target + "/set", new HashMap<String, String>() {{
            put("amount", String.valueOf(amount));
        }});
    }
}
