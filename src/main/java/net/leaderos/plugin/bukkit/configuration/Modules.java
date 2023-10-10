package net.leaderos.plugin.bukkit.configuration;


import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;

/**
 * Modules config file
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
@Names(strategy = NameStrategy.IDENTITY)
public class Modules extends OkaeriConfig {

    /**
     *
     */
    private WebStore WebStore = new WebStore();

    /**
     * WebStore module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class WebStore extends OkaeriConfig {
        /**
         * Status of webstore mode
         */
        private boolean status = true;
    }

    /**
     *
     */
    private AuthLogin AuthLogin = new AuthLogin();

    /**
     * AuthLogin module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class AuthLogin extends OkaeriConfig {
        /**
         * Status of AuthLogin mode
         */
        private boolean status = true;
    }

    /**
     *
     */
    private Bazaar Bazaar = new Bazaar();

    /**
     * Bazaar module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class Bazaar extends OkaeriConfig {
        /**
         * Status of Bazaar mode
         */
        private boolean status = true;
        /**
         * Server id of bazaar
         */
        private int serverId = 1;
    }
}