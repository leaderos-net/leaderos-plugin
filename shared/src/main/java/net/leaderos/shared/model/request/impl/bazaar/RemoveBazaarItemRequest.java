package net.leaderos.shared.model.request.impl.bazaar;

import net.leaderos.shared.model.request.DeleteRequest;

import java.io.IOException;
import java.util.HashMap;

public class RemoveBazaarItemRequest extends DeleteRequest {

    public RemoveBazaarItemRequest(String userId, String id) throws IOException {
        super("bazaar/storages/" + userId + "/items/" + id, new HashMap<>());
    }

}
