package net.leaderos.bungee.configuration;


import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
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
     * Auth system
     */
    private Auth Auth = new Auth();

    /**
     * Auth module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class Auth extends OkaeriConfig {
        /**
         * Status of Auth mode
         */
        private boolean status = false;
    }

    /**
     * Discord module
     */
    private Discord Discord = new Discord();

    /**
     * Discord module settings
     *
     * @since 1.0
     * @author rafaelflromao
     */
    @Getter
    @Setter
    public static class Discord extends OkaeriConfig {
        /**
         * Status of Discord mode
         */
        private boolean status = false;
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
        private boolean status = false;
    }

    /**
     * Connect module setting
     */
    private Connect Connect = new Connect();

    /**
     * Connect module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class Connect extends OkaeriConfig {
        /**
         * Status of Connect mode
         */
        private boolean status = false;

        /**
         * Server token name
         */
        @Comment("You can get your server token from Dashboard > Store > Game Servers")
        private String serverToken = "YOUR_SERVER_TOKEN";

        /**
         * Executes commands only if player is online
         */
        @Comment("Executes commands only if player is online")
        private boolean onlyOnline = true;

        /**
         * Executes command with delay when player comes online (in seconds)
         */
        private int executeDelay = 5;
    }
}