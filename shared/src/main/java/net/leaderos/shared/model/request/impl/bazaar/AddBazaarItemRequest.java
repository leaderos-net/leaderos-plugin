package net.leaderos.shared.model.request.impl.bazaar;

import net.leaderos.shared.model.request.PostRequest;

import java.io.IOException;
import java.util.HashMap;

public class AddBazaarItemRequest extends PostRequest {

    public AddBazaarItemRequest(String userId, String name, String lore, int amount, int maxDurability, int durability, String base64, double price, String creationDate, String modelId, String enchantments, int serverId, String item) throws IOException {
        super("bazaar/storages/" + userId + "/items", new HashMap<String, String>() {{
            put("name", name);
            if (lore != null)
                put("lore", lore);
            put("amount", String.valueOf(amount));
            put("maxDurability", String.valueOf(maxDurability));
            put("durability", String.valueOf(durability));
            put("base64", base64);
            put("price", String.valueOf(price));
            put("creationDate", String.valueOf(creationDate));
            if (modelId != null)
                put("modelID", modelId);
            if (enchantments != null)
                put("enchantments", enchantments);
            put("serverID", String.valueOf(serverId));
            put("itemID", item);
        }});
    }

}
