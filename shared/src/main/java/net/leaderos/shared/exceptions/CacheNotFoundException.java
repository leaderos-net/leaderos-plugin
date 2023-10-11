package net.leaderos.shared.exceptions;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class CacheNotFoundException extends Exception {

    /**
     * CacheNotFoundException exception
     *
     * @param message      of exception
     */
    public CacheNotFoundException(String message) {
        super(message);
    }
}