package net.leaderos.shared.model.request.impl.bazaar;

import net.leaderos.shared.model.request.GetRequest;

import java.io.IOException;

public class GetBazaarItemsRequest extends GetRequest {

    public GetBazaarItemsRequest(String userId, String serverId) throws IOException {
        super("bazaar/storages/" + userId + "/items?serverID=" + serverId);
    }

}
