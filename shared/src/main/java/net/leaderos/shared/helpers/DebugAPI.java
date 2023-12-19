package net.leaderos.shared.helpers;

/**
 * DebugAPI for requests
 * @author poyrazinan
 * @since 1.0.2
 */
public interface DebugAPI {

    /**
     * Sends debug to console
     * @param message to debug
     */
    void send(String message, boolean strict);

}
