package net.leaderos.shared.model.request.impl.verify;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class VerifyRequest extends PostRequest {
    public VerifyRequest(String code, String username, String uuid) throws IOException {
        super("integrations/leaderos-id/verify-ingame", new HashMap<String, String>() {{
            put("code", code);
            put("username", uuid);
            put("realname", username);
        }});
    }
}
