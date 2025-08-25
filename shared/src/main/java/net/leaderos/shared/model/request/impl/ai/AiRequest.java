package net.leaderos.shared.model.request.impl.ai;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class AiRequest extends PostRequest {
    public AiRequest(String prompt, String language) throws IOException {
        super("integrations/leaderos-ai/ai-command/ingame", new HashMap<String, String>() {{
            put("prompt", prompt);
            put("language", language);
        }});
    }
}
