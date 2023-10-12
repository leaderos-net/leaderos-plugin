package net.leaderos.shared.model.request;

import net.leaderos.shared.model.Request;

import java.io.IOException;
import java.util.Map;

/**
 * DeleteRequest class extended with Request
 *
 * @author poyrazinan
 * @since 1.0
 */
public class DeleteRequest extends Request {

    /**
     * Request constructor
     *
     * @param api  of request
     * @param body of request
     * @throws IOException      for HttpUrlConnection
     */
    public DeleteRequest(String api, Map<String, String> body) throws IOException {
        super(api, body, RequestType.DELETE);
    }

    /**
     * Request constructor
     *
     * @param api  of request
     * @throws IOException      for HttpUrlConnection
     */
    public DeleteRequest(String api) throws IOException {
        super(api, null, RequestType.DELETE);
    }
}
