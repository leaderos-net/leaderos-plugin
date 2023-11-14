package net.leaderos.shared.model.request;

import net.leaderos.shared.model.Request;

import java.io.IOException;
import java.util.Map;

/**
 * PostRequest class extended with Request
 *
 * @author poyrazinan
 * @since 1.0
 */
public class PostRequest extends Request {

    /**
     * Request constructor
     *
     * @param api  of request
     * @param body of request
     * @throws IOException for HttpUrlConnection
     */
    public PostRequest(String api, Map<String, String> body) throws IOException {
        super(api, body, RequestType.POST);
    }

    /**
     * Post request constructor
     *
     * @param api of request
     * @throws IOException for HttpUrlConnection
     */
    public PostRequest(String api) throws IOException {
        super(api, null, RequestType.POST);
    }
}
