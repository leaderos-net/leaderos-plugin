package net.leaderos.shared;

import lombok.Getter;
/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class Shared {

    /**
     * Shared instance
     */
    @Getter
    private static Shared instance;

    /**
     * Link of request
     */
    @Getter
    private static String link;

    /**
     * ApiKey for request
     */
    @Getter
    private static String apiKey;

    /**
     * Constructor of shared
     *
     * @param link api link
     * @param apiKey api key
     */
    public Shared(String link, String apiKey) {
        Shared.link = link;
        Shared.apiKey = apiKey;
        instance = this;
    }
}
