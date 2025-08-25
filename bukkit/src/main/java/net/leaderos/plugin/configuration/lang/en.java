package net.leaderos.plugin.configuration.lang;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.configuration.Language;

import java.util.Arrays;
import java.util.Collections;
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

        private String cannotCreateFull = "{prefix} &cPlease create some space in your inventory and try again.";

        private String haveRequestOngoing = "&cPlease wait for your current request to be done!";

        private String registrationRequired = "{prefix} &cYou must register on the website for this action!";

        /**
         * Help commands message
         */
        @Comment("Help commands message")
        private List<String> help = Arrays.asList(
                "&6&l  LEADEROS PLUGIN'S COMMANDS",
                "",
                "&8 ▪ &e/ai <prompt> &8» &fGenerates an AI response based on the prompt.",
                "&8 ▪ &e/verify <code> &8» &fVerifies your Minecraft account.",
                "&8 ▪ &e/discord-sync &8» &fGives you the Discord sync link.",
                "",
                "&8 ▪ &e/webbazaar &8» &fOpens the Bazaar Storage menu.",
                "&8 ▪ &e/webstore &8» &fOpens the Web Store menu.",
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
            private String commandMessage = "{prefix} <&aClick here to sync your account with Discord!{&eClick Me!}(open_url:%link%)>";

            /**
             * error on DiscordSync link
             */
            private String noLink = "{prefix} &cAn error occurred while connecting web-server. Please visit our website.";
        }

        /**
         * Vouchers messages
         */
        private Vouchers vouchers = new Vouchers();

        /**
         * Voucher messages
         */
        @Getter @Setter
        public static class Vouchers extends Language.Messages.Vouchers {

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

            private String cacheUpdated = "{prefix} &aSuccessfully updated cache for &b{target}&a.";
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

            private String commandBlacklisted = "{prefix} &cThis command is blacklisted and cannot be executed: &e%command%";
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
    public static class Gui extends Language.Gui {

        /**
         * Default gui object
         */
        private DefaultGui defaultGui = new DefaultGui();

        /**
         * Default Gui arguments class
         */
        @Getter @Setter
        public static class DefaultGui extends Language.Gui.DefaultGui {

            /**
             * PreviousPage item object
             */
            private PreviousPage previousPage = new PreviousPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class PreviousPage extends Language.Gui.DefaultGui.PreviousPage {
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
            public static class NextPage extends Language.Gui.DefaultGui.NextPage {
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
        public static class WebStoreGui extends Language.Gui.WebStoreGui {

            /**
             * name of gui
             */
            private String guiName = "&8WebStore";

            /**
             * Price format
             */
            @Comment({
                    "Price format",
                    "{price} for original price (e.g. 10.00 USD)",
                    "{rawPrice} for raw price (e.g. 10.00)"
            })
            private String priceFormat = "{price}";

            /**
             * Discounted price format
             */
            @Comment({
                    "Discounted price format",
                    "{price} for original price (e.g. 10.00 USD)",
                    "{rawPrice} for raw price (e.g. 10.00)",
                    "{discountedPrice} for discounted price (e.g. 5.00 USD)",
                    "{rawDiscountedPrice} for raw discounted price (e.g. 5.00)"
            })
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
             * purchase title
             */
            private String buyWebStoreTitle = "&6&lPURCHASE";

            /**
             * purchase subtitle progress
             */
            private String buyWebStoreProgress = "&7Purchasing in progress...";

            /**
             * purchase subtitle success
             */
            private String buyWebStoreSuccess = "&aPurchase successful!";

            /**
             * purchase subtitle success
             */
            private String buyWebStoreNotEnoughCredit = "&cNot enough credits.";

            private String buyWebStoreOutOfStock = "&cOut of stock.";

            private String buyWebStoreProductNotFound = "&cProduct not found.";

            private String buyWebStoreUserNotFound = "&cUser not found.";

            private String webStoreCategoryNotFound = "&cCategory not found.";

            private String buyWebStoreRequiredProduct = "&cThere are required products you do not own!";

            private String buyWebStoreRequiredLinkedAccount = "&cYou must link your account to buy this product!";

            private String buyWebStoreDowngradeNotAllowed = "&cYou cannot downgrade your subscription!";

            private String buyWebStoreInvalidVariable = "&cInvalid variables provided!";

            /**
             * withdraw item subtitle error
             */
            private String buyWebStoreError = "&cAn error occurred during the purchase process!";

            /**
             * Default Category attributes
             */
            private DefaultCategory defaultCategory = new DefaultCategory();

            /**
             * Default Category arguments class
             */
            @Getter @Setter
            public static class DefaultCategory extends Language.Gui.WebStoreGui.DefaultCategory {

                /**
                 * Default Lore
                 */
                private List<String> lore = Arrays.asList(
                        "&r",
                        "&aClick to open category!"
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
            public static class DefaultProduct extends Language.Gui.WebStoreGui.DefaultProduct {

                /**
                 * Default Title
                 */
                private String title = "&e%name%";

                /**
                 * Default Lore
                 */
                private List<String> lore = Arrays.asList(
                        "&r",
                        "&dProduct Details:",
                        "&8 ▪ &7Price &8» &e%price%",
                        "&8 ▪ &7Stock &8» &e%stock%",
                        "",
                        "&aClick to buy!"
                );
            }

            /**
             * Credit attributes
             */
            private Credit credit = new Credit();

            /**
             * Credit arguments class
             */
            @Getter @Setter
            public static class Credit extends Language.Gui.WebStoreGui.Credit {

                /**
                 * Title of credit item
                 */
                private String title = "&eCredits: &a%credits%";
            }

            /**
             * ConfirmPurchase attributes
             */
            private ConfirmPurchase confirmPurchase = new ConfirmPurchase();

            /**
             * ConfirmPurchase arguments class
             */
            @Getter @Setter
            public static class ConfirmPurchase extends Language.Gui.WebStoreGui.ConfirmPurchase {

                /**
                 * GUI Name
                 */
                private String guiName = "&8Confirm Purchase";

                /**
                 * Confirm Title
                 */
                private String confirmTitle = "&a&lBUY";

                /**
                 * Cancel Title
                 */
                private String cancelTitle = "&c&lCANCEL";
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
        public static class Bazaar extends Language.Gui.Bazaar {

            /**
             * name of gui
             */
            private String guiName = "&8Bazaar";

            /**
             * lore modifier
             */
            private String clickLore = "&aClick this to re-take!";

            /**
             * withdraw item title
             */
            private String withdrawTitle = "&6&lWITHDRAW";

            /**
             * withdraw item subtitle progress
             */
            private String withdrawProgressSubtitle = "&7Withdraw in progress...";

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
            private List<String> addItemLore = Arrays.asList("", "&aClick to add item!");

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
        public static class DonationsGUI extends Language.Gui.DonationsGUI {

            /**
             * name of gui
             */
            private String guiName = "&8Donations";

            /**
             * name of donator item
             */
            private String displayName = "&e#%i% &6%player%";

            /**
             * updates donation data
             */
            private String updatedDonationDataMessage = "{prefix} &aUpdated donations data.";

            /**
             * lore of donator item
             */
            private List<String> lore = Arrays.asList(
                    "",
                    "&7Donation: &e%amount% %symbol%",
                    ""
            );

            /**
             * Info attributes
             */
            private Info info = new Info();

            /**
             * Donations info
             */
            @Getter @Setter
            public static class Info extends Language.Gui.DonationsGUI.Info {

                /**
                 * Latest Donations info attributes
                 */
                private Latest latest = new Latest();

                /**
                 * Latest Donations info
                 */
                @Getter @Setter
                public static class Latest extends Language.Gui.DonationsGUI.Info.Latest {

                    /**
                     * Item name
                     */
                    private String displayName = "&eLatest Donations";
                }

                /**
                 * Top Donations (All Time) info attributes
                 */
                private TopAllTime topAllTime = new TopAllTime();

                /**
                 * Top Donations (All Time) info
                 */
                @Getter @Setter
                public static class TopAllTime extends Language.Gui.DonationsGUI.Info.TopAllTime {

                    /**
                     * Item name
                     */
                    private String displayName = "&eTop Donations (All Time)";
                }

                /**
                 * Top Donations (Annual) info attributes
                 */
                private TopAnnual topAnnual = new TopAnnual();

                /**
                 * Top Donations (Annual) info
                 */
                @Getter @Setter
                public static class TopAnnual extends Language.Gui.DonationsGUI.Info.TopAnnual {

                    /**
                     * Item name
                     */
                    private String displayName = "&eTop Donations (Annual)";
                }

                /**
                 * Top Donations (Monthly) info attributes
                 */
                private TopMonthly topMonthly = new TopMonthly();

                /**
                 * Top Donations (Monthly) info
                 */
                @Getter @Setter
                public static class TopMonthly extends Language.Gui.DonationsGUI.Info.TopMonthly {

                    /**
                     * Item name
                     */
                    private String displayName = "&eTop Donations (Monthly)";
                }

                /**
                 * Top Donations (Daily) info attributes
                 */
                private TopDaily topDaily = new TopDaily();

                /**
                 * Top Donations (Daily) info
                 */
                @Getter @Setter
                public static class TopDaily extends Language.Gui.DonationsGUI.Info.TopDaily {

                    /**
                     * Item name
                     */
                    private String displayName = "&eTop Donations (Daily)";
                }
            }
        }
    }
}
