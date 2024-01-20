package net.leaderos.shared.model.request.impl.store;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class BuyRequest extends PostRequest {

    public BuyRequest(String user, String productId) throws IOException {
        super("store/buy", new HashMap<String, String>() {{
            put("user", user);
            put("products[0][id]", productId);
            put("products[0][quantity]", "1");
        }});
    }

}
