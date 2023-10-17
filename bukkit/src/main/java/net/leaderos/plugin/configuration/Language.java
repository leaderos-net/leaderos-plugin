package net.leaderos.plugin.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter @Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class Language extends OkaeriConfig {

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

        @Comment("Prefix of messages")
        private String prefix = "&3LeaderOS &8»";

        private String playerNotOnline = "{prefix} &cTarget player is not online.";

        private String playerNotAvailable = "{prefix} &cPlayer is not available.";

        private String targetPlayerNotAvailable = "{prefix} &cTarget player is not available.";

        private String cannotCreateFull = "{prefix} &cPlease create some space in your inventory and try again.";

        /**
         * Help commands message
         */
        @Comment("Help commands message")
        private List<String> help = Arrays.asList(
                "&6&l  LEADEROS PLUGIN'S COMMANDS",
                "",
                "&8 ▪ &e/auth &8» &fGives you the auth link.",
                "&8 ▪ &e/discord-sync &8» &fGives you the Discord sync link.",
                "",
                "&8 ▪ &e/webbazaar &8» &fOpens the Bazaar Storage menu.",
                "&8 ▪ &e/webbazaar &8» &fOpens the Bazaar Storage menu.",
                "&8 ▪ &e/donations &8» &fOpens the Donations menu.",
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
            private String moduleEnabled = "{prefix} &e%module_name% &aenabled.";

            /**
             * Module closed message
             */
            private String moduleClosed = "{prefix} &e%module_name% &cclosed.";

            /**
             * Module disabled message
             */
            private String moduleDisabled = "{prefix} &e%module_name% &cdisabled.";

            /**
             * Missing dependency message
             */
            private String missingDependency = "{prefix} &e%module_name% &chas not started due missing dependency. &8[&e%dependencies%&8]";
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
            private String commandMessage = "{prefix} <&aFor authentication &e&nclick here!{&eClick Me!}(open_url:%link%)>";

            /**
             * Module error message
             */
            private String moduleError = "{prefix} <&cThis system require you to login website, &e&nlick here!{&eClick Me!}(open_url:%link%)>";

            /**
             * error on auth link
             */
            private String noLink = "{prefix} &cAn error occured while connecting web-server. Please visit our website.";
        }

        /**
         * Discord messages
         */
        private Discord discord = new Discord();

        /**
         * Discord messages of plugin
         */
        @Getter @Setter
        public static class Discord extends OkaeriConfig {

            /**
             * Command Message
             */
            private String commandMessage = "{prefix} <&aTo sync your account with Discord, &e&nclick here!{&eClick Me!}(open_url:%link%)>";

            /**
             * error on DiscordSync link
             */
            private String noLink = "{prefix} &cAn error occured while connecting web-server. Please visit our website.";
        }

        /**
         * Vouchers messages
         */
        private Vouchers vouchers = new Vouchers();

        /**
         * Voucher messages
         */
        @Getter @Setter
        public static class Vouchers extends OkaeriConfig {

            @Comment({
                    "Voucher item name",
                    "You can use {amount} for amount of voucher"
            })
            private String itemDisplayName = "&8[&a{amount} credit(s)&8] &7#{id}";

            @Comment("Voucher item lore")
            private List<String> itemLore = Collections.singletonList("&7Right click to use this voucher.");

            @Comment({
                    "Players should not have the vouchers anyway, they get deleted after used once.",
                    "This is a thing only if there is a dupe bug."
            })
            private String alreadyUsed = "{prefix} &cThis voucher already used.";

            private String successfullyUsed = "{prefix} &aSuccessfully used a voucher that worth &e{amount} credit(s)&a.";

            private String successfullyCreated = "{prefix} &aSuccessfully created a voucher that worth &e{amount} credit(s)&a.";

            private String cannotCreateNegative = "{prefix} &cPlease enter a valid amount. The amount must be higher than 0.";

            private String cannotCreateNotEnough = "{prefix} &cYou do not have enough credit(s). Required: &e{amount} credits(s)";

            private String successfullyGave = "{prefix} &aSuccessfully gave an voucher to &b{target} &afor amount &e{amount} credits(s)";

            private String helpStaff = "{prefix} &fUsage: &e/vouchers give <player> <amount>";

            private String help = "{prefix} &fUsage: &e/vouchers create <amount>";

        }

        /**
         * Credit messages
         */
        private Credit credit = new Credit();

        /**
         * Credit messages of plugin
         */
        @Getter @Setter
        public static class Credit extends OkaeriConfig {

            private String creditInfo = "{prefix} &aYou have &e{amount} &acredit(s)";

            private String creditInfoOther = "{prefix} &b{target} &ahas &e{amount} &acredit(s)";

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
        public static class Connect extends OkaeriConfig {

            private String connectExecutedCommand = "{prefix} &aConnect module executed &e%command%";

            private String joinedSocketRoom = "{prefix} &aConnect module connected room successfully.";
        }
    }


    /**
     * Gui object
     */
    private Gui gui = new Gui();

    /**
     * Gui arguments class
     */
    @Getter
    @Setter
    public static class Gui extends OkaeriConfig {

        /**
         * Default gui object
         */
        private DefaultGui defaultGui = new DefaultGui();

        /**
         * Default Gui arguments class
         */
        @Getter @Setter
        public static class DefaultGui extends OkaeriConfig {

            /**
             * PreviousPage item object
             */
            private PreviousPage previousPage = new PreviousPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class PreviousPage extends OkaeriConfig {
                /**
                 * material of item
                 */
                private String name = "&ePrevious Page";
            }

            /**
             * PreviousPage item object
             */
            private NextPage nextPage = new NextPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class NextPage extends OkaeriConfig {
                /**
                 * material of item
                 */
                private String name = "&eNext Page";
            }

        }

        /**
         * WebStore gui object
         */
        private WebStoreGui webStoreGui = new WebStoreGui();

        /**
         * WebStore gui arguments class
         */
        @Getter @Setter
        public static class WebStoreGui extends OkaeriConfig {

            /**
             * name of gui
             */
            private String guiName = "&8WebStore";

            /**
             * Discounted price format
             */
            private String discountedPriceFormat = "&c&m{price}&r &a{discountedPrice}";

            /**
             * Discount amount format
             */
            private String discountAmountFormat = "&8[&a%{discount}&8]";

            /**
             * Stock unlimited format
             */
            private String stockUnlimited = "&6&lUNLIMITED";

            /**
             * withdraw item title
             */
            private String buyWebStoreTitle = "&6&lBUY PRODUCT";

            /**
             * withdraw item subtitle progress
             */
            private String buyWebStoreProgress = "&7Buy in progress...";

            /**
             * withdraw item subtitle success
             */
            private String buyWebStoreSuccess = "&aBuy success.";

            /**
             * withdraw item subtitle success
             */
            private String buyWebStoreNotEnoughCredit = "&cNot enough credits.";

            /**
             * withdraw item subtitle error
             */
            private String buyWebStoreError = "&cBuy error.";

            /**
             * register website for this action
             */
            private String buyWebStoreNoAuthLinkError = "{prefix} &cYou must register website for this action!";

            /**
             * Default Category attributes
             */
            private DefaultCategory defaultCategory = new DefaultCategory();

            /**
             * Default Category arguments class
             */
            @Getter @Setter
            public static class DefaultCategory extends OkaeriConfig {

                /**
                 * Default Lore
                 */
                private List<String> lore = Arrays.asList(
                        "&r",
                        "&aClick for open category!"
                );
            }

            /**
             * DefaultProduct attributes
             */
            private DefaultProduct defaultProduct = new DefaultProduct();

            /**
             * DefaultProduct arguments class
             */
            @Getter @Setter
            public static class DefaultProduct extends OkaeriConfig {

                /**
                 * Default Lore
                 */
                private List<String> lore = Arrays.asList(
                        "&r",
                        "&dProduct Details:",
                        "&8 ▪ &7Price &8» &e%price%",
                        "&8 ▪ &7Stock &8» &e%stock%",
                        "",
                        "&aClick for buy!"
                );
            }
        }

        /**
         * Bazaar gui object
         */
        private Bazaar bazaarGui = new Bazaar();

        /**
         * Bazaar gui arguments class
         */
        @Getter @Setter
        public static class Bazaar extends OkaeriConfig {

            /**
             * name of gui
             */
            private String guiName = "&8Bazaar";

            /**
             * lore modifier
             */
            private String clickLore = "&aClick this for re-take!";

            /**
             * withdraw item title
             */
            private String withdrawTitle = "&6&lWITHDRAW";

            /**
             * withdraw item subtitle progress
             */
            private String withdrawProgressSubtitle = "&7Withraw in progress...";

            /**
             * withdraw item subtitle success
             */
            private String withdrawSuccessSubtitle = "&aWithdraw success.";

            /**
             * withdraw item subtitle error
             */
            private String withdrawErrorSubtitle = "&cWithdraw error.";

            /**
             * Add item title
             */
            private String addItemName = "&eAdd Item";

            /**
             * Add item lore
             */
            private List<String> addItemLore = Arrays.asList("", "&aClick for add item!");

            /**
             * Add item gui name
             */
            private String addItemGuiName = "&8Bazaar » Add item";

            private String addItemMessage = "{prefix} &aAdded &e%item_name% &ato bazaar storage.";

            /**
             * return item message
             */
            private String returnItemMessage = "{prefix} &cYou have reached max storage amount &8(&4%max_amount%&8) &creturned &e%amount% &citem.";
        }

        /**
         * Bazaar gui object
         */
        private DonationsGUI donationsGui = new DonationsGUI();

        /**
         * Bazaar gui arguments class
         */
        @Getter @Setter
        public static class DonationsGUI extends OkaeriConfig {

            /**
             * name of gui
             */
            private String guiName = "&8Recent Donations";

            /**
             * name of donator item
             */
            private String displayName = "&6%player%";

            /**
             * updates donation data
             */
            private String updatedDonationDataMessage = "{prefix} &aUpdated donations data.";

            /**
             * lore of donator item
             */
            private List<String> lore = Arrays.asList(
                    "",
                    "&7Donation: &e%credit% %symbol%",
                    ""
            );
        }
    }
}
