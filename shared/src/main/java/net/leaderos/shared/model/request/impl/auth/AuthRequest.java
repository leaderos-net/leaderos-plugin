package net.leaderos.shared.model.request.impl.auth;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class AuthRequest extends PostRequest {

    public AuthRequest(String username, String uuid) throws IOException {
        super("auth/generate-link", new HashMap<String, String>() {{
            put("username", username);
            put("uuid", uuid);
        }});
    }

}
