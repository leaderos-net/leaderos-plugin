package net.leaderos.shared.model.request.impl.user;

import net.leaderos.shared.model.request.GetRequest;

import java.io.IOException;

public class GetUserRequest extends GetRequest {

    public GetUserRequest(String username) throws IOException {
        super("users?username=" + username);
    }

}
