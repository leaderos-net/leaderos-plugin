package net.leaderos.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.shared.error.Error;
import org.json.JSONObject;

/**
 * Response class for request
 *
 * @author poyrazinan
 * @since 1.0
 */
@Setter
@Getter
@AllArgsConstructor
public class Response {

    /**
     * Response code of request
     */
    private int responseCode;

    /**
     * status of response
     */
    private boolean status;

    /**
     * Response message of request
     */
    private JSONObject responseMessage;

    /**
     * Error
     */
    private Error error;

    /**
     * Getter of responseMessage
     * @return JSONObject - message
     */
    public JSONObject getResponseMessage() {
        if (status)
            return this.responseMessage;
        else return null;
    }
}
