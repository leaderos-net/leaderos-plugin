package net.leaderos.shared.configuration.lang;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.shared.configuration.Language;

import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class tr extends Language {

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

        /**
         * Prefix of plugin
         */
        private String prefix = "&3turkce &8»";

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
        public static class Auth extends Language.Messages.Auth {

            /**
             * Command Message
             */
            private String commandMessage = "<&aFor authentication click here!{&5Click Me!}(open_url:%link%)>";

            /**
             * Module error message
             */
            private String moduleError = "<&cThis system require you to login website, click here!{&5Click Me!}(open_url:%link%)>";
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
            private String moduleDisabled = "{prefix} &4%module_name% disabled.";
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
             * Filler item object
             */
            private FillerItem fillerItem = new FillerItem();

            /**
             * Filler item arguments class
             */
            @Getter @Setter
            public static class FillerItem extends Language.Gui.DefaultGui.FillerItem {
                /**
                 * material of item
                 */
                private String material = "GRAY_STAINED_GLASS_PANE";
                /**
                 * status of filler
                 */
                @Comment("Fills empty areas")
                private boolean useFiller = true;
            }

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
                private String name = "&aPrevious Page";
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
                private String name = "&aNext Page";
            }

            /**
             * Default Category attributes
             */
            private DefaultCategory defaultCategory = new DefaultCategory();

            /**
             * Default Category arguments class
             */
            @Getter @Setter
            public static class DefaultCategory extends Language.Gui.DefaultGui.DefaultCategory {

                /**
                 * Default material
                 */
                private String material = "DIAMOND_BLOCK";

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
            public static class DefaultProduct extends Language.Gui.DefaultGui.DefaultProduct {

                /**
                 * Default material
                 */
                private String material = "DIAMOND";

                /**
                 * Default Lore
                 */
                private List<String> lore = Arrays.asList(
                        "&r",
                        "&7Price &8» &e%price%",
                        "&7Stock &8» &e%stock%",
                        "",
                        "&aClick for buy!"
                );
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
             * layout of gui
             */
            @Comment("Layout of gui")
            private List<String> layout = Arrays.asList(
                    "         ",
                    "   ccc   ",
                    "         ",
                    "  ppppp  ",
                    "         ",
                    "b       n"
            );

            /**
             * name of gui
             */
            private String guiName = "&8WebStore";

            private String discountedPriceFormat = "&c&m{price}&r &a{discountedPrice}";

            private String discountAmountFormat = "&8[&a%{discount}&8]";

            private String stockUnlimited = "&6&lUNLIMITED";
        }

    }
}
