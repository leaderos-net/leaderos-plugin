package net.leaderos.velocity.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter @Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class Language  extends OkaeriConfig {

    /**
     * Settings menu of config
     */
    @Comment("Main settings")
    private Messages messages = new Messages();

    /**
     * Messages of plugin
     * @author poyrazinan
     * @since 1.0
     */
    @Getter
    @Setter
    public static class Messages extends OkaeriConfig {
        /**
         * Prefix of plugin TODO Explain
         */
        private String prefix = "&3LeaderOS &8»";

        private String creditInfo = "{prefix} &aYou have &e{amount} &acredit(s)";

        private String creditInfoOther = "{prefix} &b{target} &ahas &e{amount} &acredit(s)";

        private String cannotSendCreditYourself = "{prefix} &cYou can not send credit(s) to yourself.";

        private String cannotSendCreditNegative = "{prefix} &cPlease enter a valid amount. The amount must be higher than 0.";

        private String cannotSendCreditNotEnough = "{prefix} &cYou do not have enough credit(s).";

        private String playerNotOnline = "{prefix} &cTarget player is not online.";

        private String playerNotAvailable = "{prefix} &cPlayer is not available.";

        private String targetPlayerNotAvailable = "{prefix} &cTarget player is not available.";

        private String successfullySentCredit = "{prefix} &aSuccessfully sent &e{amount} credit(s) &ato &b{target}&a.";

        private String successfullySetCredit = "{prefix} &aSuccessfully set credits to &e{amount} &afor &b{target}&a.";

        private String successfullyAddedCredit = "{prefix} &aSuccessfully added &e{amount} credit &ato &b{target}&a.";

        private String successfullyRemovedCredit = "{prefix} &aSuccessfully removed &e{amount} credit &afrom &b{target}&a.";

        private String receivedCredit = "{prefix} &aYou just received &e{amount} credit(s) &afrom &e{player}.";

        private String cannotCreateFull = "{prefix} &cPlease create some space in your inventory and try again.";

        /**
         * Help commands message
         */
        @Comment("Help commands message")
        private List<String> help = Arrays.asList(
                "&6&l  LEADEROS PLUGIN'S COMMANDS",
                "",
                "&8 ▪ &e/webshop &8» &fOpens the WebShop menu.",
                "&8 ▪ &e/webshop server <serverName> &8» &fShows the server in the Webshop menu.",
                "&8 ▪ &e/webshop servers &8» &fShows all servers in the Webshop menu.",
                "",
                "&8 ▪ &e/creditsvoucher give <player> <amount> &8» &fGives the player a credit voucher.",
                "&8 ▪ &e/creditsvoucher create <amount> &8» &fCreates a credit voucher.",
                "",
                "&8 ▪ &e/credits &8» &fShows your credit amount.",
                "&8 ▪ &e/credits see <target> &8» &fShows the player credit amount.",
                "&8 ▪ &e/credits send <target> <amount> &8» &fSends credits to the player.",
                "&8 ▪ &e/credits set <target> <amount> &8» &fSets the player's credits.",
                "&8 ▪ &e/credits remove <target> <amount> &8» &fRemoves credits from the player.",
                "&8 ▪ &e/credits add <target> <amount> &8» &fAdds credits to the player.",
                "",
                "&8 ▪ &e/leaderos reload &8» &fReloads the config."
        );

        /**
         * Auth messages
         */
        private Auth auth = new Auth();

        /**
         * Auth messages of plugin
         */
        @Getter @Setter
        public static class Auth extends OkaeriConfig {

            /**
             * Command Message
             */
            private String commandMessage = "{prefix} &aFor authentication click here!";

            /**
             * Module error message
             */
            private String moduleError = "{prefix} <&cThis system require you to login website, click here!{&5Click Me!}(open_url:%link%)>";

            /**
             * error on auth link
             */
            private String noLink = "{prefix} &cAn error occured while connecting web-server. Please visit our website.";
        }

        /**
         * Info messages
         */
        private Info info = new Info();

        /**
         * Info messages of plugin
         *
         * @author poyrazinan
         * @since 1.0
         */
        @Getter
        @Setter
        public static class Info extends OkaeriConfig {

            /**
             * Module enabled message
             */
            private String moduleEnabled = "{prefix} &a%module_name% enabled.";

            /**
             * Module closed message
             */
            private String moduleClosed = "{prefix} &c%module_name% closed.";

            /**
             * Module disabled message
             */
            private String moduleDisabled = "{prefix} &4%module_name% disabled.";

            /**
             * Missing dependency message
             */
            private String missingDependency = "{prefix} &4%module_name% has not started due missing dependency. &c[%dependencies%]";
        }

        /**
         * Command object
         */
        private Command command = new Command();

        /**
         * Command arguments class
         */
        @Getter @Setter
        public static class Command extends OkaeriConfig {

            /**
             * Invalid argument message
             */
            private String invalidArgument = "{prefix} &cInvalid argument!";

            /**
             * Unknown command message
             */
            private String unknownCommand = "{prefix} &cUnknown command!";

            /**
             * Not enough arguments message
             */
            private String notEnoughArguments = "{prefix} &cNot enough arguments!";

            /**
             * too many arguments message
             */
            private String tooManyArguments = "{prefix} &cToo many arguments!";

            /**
             * no perm message
             */
            private String noPerm = "{prefix} &cYou do not have permission to do this action!";

        }
    }
}
