package net.leaderos.shared.model.request;

import net.leaderos.shared.model.Request;

import java.io.IOException;
import java.util.Map;

/**
 * GetRequest class extended with Request
 *
 * @author poyrazinan
 * @since 1.0
 */
public class PatchRequest extends Request {

    /**
     * Request constructor
     *
     * @param api  of request
     * @param body of request
     * @throws IOException      for HttpUrlConnection
     */
    public PatchRequest(String api, Map<String, String> body) throws IOException {
        super(api, body, RequestType.PATCH);
    }

    /**
     * Request constructor
     *
     * @param api  of request
     * @throws IOException      for HttpUrlConnection
     */
    public PatchRequest(String api) throws IOException {
        super(api, null, RequestType.PATCH);
    }
}
