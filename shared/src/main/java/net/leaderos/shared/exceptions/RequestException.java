package net.leaderos.shared.exceptions;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class RequestException extends Exception {

    /**
     * Response code of request
     */
    private final int responseCode;

    /**
     * Response body
     */
    private final String response;

    /**
     * RequestException exception
     *
     * @param message      of exception
     * @param responseCode of response
     * @param response     string of json message
     */
    public RequestException(String message, int responseCode, String response) {
        super(message);
        this.responseCode = responseCode;
        this.response = response;
    }
}
