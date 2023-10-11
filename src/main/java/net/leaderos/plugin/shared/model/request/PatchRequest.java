package net.leaderos.plugin.shared.model.request;

import net.leaderos.plugin.shared.exceptions.RequestException;
import net.leaderos.plugin.shared.model.Request;

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
     * @throws RequestException for response errors
     */
    public PatchRequest(String api, Map<String, String> body) throws IOException, RequestException {
        super(api, body, RequestType.PATCH);
    }

    /**
     * Request constructor
     *
     * @param api  of request
     * @throws IOException      for HttpUrlConnection
     * @throws RequestException for response errors
     */
    public PatchRequest(String api) throws IOException, RequestException {
        super(api, null, RequestType.PATCH);
    }
}
