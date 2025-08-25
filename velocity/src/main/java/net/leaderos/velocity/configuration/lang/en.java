package net.leaderos.velocity.configuration.lang;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.velocity.configuration.Language;

import java.util.Arrays;
import java.util.List;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter @Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class en extends Language {

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
    public static class Messages extends Language.Messages {

        @Comment("Prefix of messages")
        private String prefix = "&3LeaderOS &8»";

        private String reload = "{prefix} &aPlugin reloaded successfully.";

        private String update = "{prefix} &eThere is a new update available for LeaderOS Plugin! Please update to &a%version%";

        private String changeApiUrl = "{prefix} &cPlease change the API URL in the config.";

        private String changeApiUrlHttps = "{prefix} &cPlease change the API URL to HTTPS (https://) in the config.";

        private String playerNotOnline = "{prefix} &cTarget player is not online.";

        private String playerNotAvailable = "{prefix} &cPlayer is not available.";

        private String targetPlayerNotAvailable = "{prefix} &cTarget player is not available.";

        private String haveRequestOngoing = "&cPlease wait for your current request to be done!";

        /**
         * Help commands message
         */
        @Comment("Help commands message")
        private List<String> help = Arrays.asList(
                "&6&l  LEADEROS PLUGIN'S COMMANDS",
                "",
                "&8 ▪ &e/ai <prompt> &8» &fGenerates a response from AI.",
                "&8 ▪ &e/verify <code> &8» &fVerifies your Minecraft account.",
                "&8 ▪ &e/discord-sync &8» &fGives you the Discord sync link.",
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
        public static class Info extends Language.Messages.Info {

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
            private String moduleDisabled = "{prefix} &c%module_name% disabled.";

            /**
             * Missing dependency message
             */
            private String missingDependency = "{prefix} &c%module_name% has not started due missing dependency. &8[&e%dependencies%&8]";
        }

        /**
         * Command object
         */
        private Command command = new Command();

        /**
         * Command arguments class
         */
        @Getter @Setter
        public static class Command extends Language.Messages.Command {

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

        /**
         * Verify messages
         */
        private Verify verify = new Verify();

        /**
         * Discord messages of plugin
         */
        @Getter @Setter
        public static class Verify extends Language.Messages.Verify {
            /**
             * success message
             */
            private String successMessage = "{prefix} &aYour account has been successfully verified. Please back to the website.";

            /**
             * error message
             */
            private String failMessage = "{prefix} &cAn error occurred while verifying your account. Please try again later.";
        }

        /**
         * AI messages
         */
        private Ai ai = new Ai();

        /**
         * AI messages of plugin
         */
        @Getter @Setter
        public static class Ai extends Language.Messages.Ai {
            /**
             * AI message
             */
            private String aiMessage = "{prefix} &f{message}";

            /**
             * AI command usage
             */
            private String usage = "{prefix} &fUsage: &e/ai <prompt>";

            /*
             * Generating AI response message
             */
            private String generating = "{prefix} &7Generating AI response, please wait a moment.";

            /**
             * Error message
             */
            private String failMessage = "{prefix} &cAn error occurred while generating AI response. Please try again later.";
        }

        /**
         * Discord messages
         */
        private Discord discord = new Discord();

        /**
         * Discord messages of plugin
         */
        @Getter @Setter
        public static class Discord extends Language.Messages.Discord {

            /**
             * Command Message
             */
            private String commandMessage = "{prefix} &aClick here to sync your account with Discord!";

            /**
             * error on DiscordSync link
             */
            private String noLink = "{prefix} &cAn error occurred while connecting web-server. Please visit our website.";

            /**
             * Hover message
             */
            private String hoverMessage = "&eClick here to open url!";
        }

        /**
         * Credit messages
         */
        private Credit credit = new Credit();

        /**
         * Credit messages of plugin
         */
        @Getter @Setter
        public static class Credit extends Language.Messages.Credit {

            private String creditInfo = "{prefix} &aYou have &e{amount} &acredit(s)";

            private String creditInfoOther = "{prefix} &b{target} &ahas &e{amount} &acredit(s)";

            private String cannotSendCreditsThisUser = "{prefix} &cCould not send credits to this user.";

            private String cannotSendCreditYourself = "{prefix} &cYou can not send credit(s) to yourself.";

            private String cannotSendCreditNegative = "{prefix} &cPlease enter a valid amount. The amount must be higher than 0.";

            private String cannotSendCreditNotEnough = "{prefix} &cYou do not have enough credit(s).";

            private String successfullySentCredit = "{prefix} &aSuccessfully sent &e{amount} credit(s) &ato &b{target}&a.";

            private String successfullySetCredit = "{prefix} &aSuccessfully set credits to &e{amount} &afor &b{target}&a.";

            private String successfullyAddedCredit = "{prefix} &aSuccessfully added &e{amount} credit &ato &b{target}&a.";

            private String successfullyRemovedCredit = "{prefix} &aSuccessfully removed &e{amount} credit &afrom &b{target}&a.";

            private String receivedCredit = "{prefix} &aYou just received &e{amount} credit(s) &afrom &b{player}&a.";
        }

        /**
         * Connect messages
         */
        private Connect connect = new Connect();

        /**
         * Connect messages of plugin
         */
        @Getter @Setter
        public static class Connect extends Language.Messages.Connect {

            private String connectExecutedCommand = "{prefix} &aConnect module executed the command: &e%command%";

            private String connectWillExecuteCommand = "{prefix} &aConnect module will execute the command after the player has joined the server: &e%command%";

            private String connectExecutedCommandFromQueue = "{prefix} &aConnect module executed the command from the queue: &e%command%";

            private String subscribedChannel = "{prefix} &aConnect module has been successfully connected to the server!";

            private String commandBlacklisted = "{prefix} &cBu komut kara listeye alındığı için çalıştırılamadı: &e%command%";
        }
    }
}
