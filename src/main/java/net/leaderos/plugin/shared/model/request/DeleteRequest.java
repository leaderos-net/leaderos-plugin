package net.leaderos.plugin.shared.model.request;

import net.leaderos.plugin.bukkit.exceptions.RequestException;
import net.leaderos.plugin.shared.model.Request;

import java.io.IOException;

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
     * @throws RequestException for response errors
     */
    public DeleteRequest(String api, String body) throws IOException, RequestException {
        super(api, body, RequestType.DELETE);
    }

    /**
     * Request constructor
     *
     * @param api  of request
     * @throws IOException      for HttpUrlConnection
     * @throws RequestException for response errors
     */
    public DeleteRequest(String api) throws IOException, RequestException {
        super(api, null, RequestType.DELETE);
    }
}
