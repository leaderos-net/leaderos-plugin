package net.leaderos.plugin.shared.model.request;

import lombok.Builder;
import net.leaderos.plugin.bukkit.exceptions.RequestException;
import net.leaderos.plugin.shared.model.Request;

import java.io.IOException;

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
     * @throws IOException      for HttpUrlConnection
     * @throws RequestException for response errors
     */
    public PostRequest(String api, String body) throws IOException, RequestException {
        super(api, body, RequestType.POST);
    }

    /**
     * Post request constructor
     *
     * @param api
     * @throws IOException
     * @throws RequestException
     */
    public PostRequest(String api) throws IOException, RequestException {
        super(api, null, RequestType.POST);
    }
}
