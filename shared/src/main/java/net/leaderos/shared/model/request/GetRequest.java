package net.leaderos.shared.model.request;

import net.leaderos.shared.exceptions.RequestException;
import net.leaderos.shared.model.Request;

import java.io.IOException;

/**
 * GetRequest class extended with Request
 *
 * @author poyrazinan
 * @since 1.0
 */
public class GetRequest extends Request {

    /**
     * Request constructor
     *
     * @param api of request
     * @throws IOException for HttpUrlConnection
     */
    public GetRequest(String api) throws IOException {
        super(api, null, RequestType.GET);
    }
}
