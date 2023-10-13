package net.leaderos.plugin.configuration;


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
     * Cache module
     */
    private Cache Cache = new Cache();

    /**
     * Cache module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class Cache extends OkaeriConfig {
        /**
         * Status of Cache mode
         */
        private boolean status = true;
    }

    /**
     * Credit module
     */
    private Credit Credit = new Credit();

    /**
     * Credit module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class Credit extends OkaeriConfig {
        /**
         * Status of Cache mode
         */
        private boolean status = true;
    }

    /**
     * Voucher module
     */
    private Voucher Voucher = new Voucher();

    /**
     * Voucher module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class Voucher extends OkaeriConfig {
        /**
         * Status of Cache mode
         */
        private boolean status = false;
    }


    /**
     * bazaar module setting
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

        /**
         * default storage size
         */
        private int defaultStorageSize = 5;
    }

    /**
     * Donators module setting
     */
    private RecentDonations RecentDonations = new RecentDonations();

    /**
     * Donators module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class RecentDonations extends OkaeriConfig {
        /**
         * Status of Donators mode
         */
        private boolean status = false;
        /**
         * Donators scheduler second
         */
        private long updateSecond = 900;

        /**
         * Limit of recent donations
         */
        private int recentDonationLimit = 5;
    }
}