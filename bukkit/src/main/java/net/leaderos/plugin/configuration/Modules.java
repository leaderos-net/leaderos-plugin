package net.leaderos.plugin.configuration;


import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

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
        private boolean status = true;
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
    }

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
             * landing layout of gui
             */
            @Comment({
                    "Layout of gui",
                    "c: Category",
                    "b: Prev Page",
                    "n: Next Page"
            })
            private List<String> landingGuiLayout = Arrays.asList(
                    "         ",
                    "  ccccc  ",
                    "b       n"
            );

            /**
             * layout of gui
             */
            @Comment({
                    "Layout of gui",
                    "e: Any Element (Product, Category)",
                    "c: Category",
                    "p: Product",
                    "b: Prev Page",
                    "n: Next Page"
            })
            private List<String> layout = Arrays.asList(
                    "         ",
                    "  eeeee  ",
                    "  eeeee  ",
                    "  eeeee  ",
                    "         ",
                    "b       n"
            );

            /**
             * Filler item object
             */
            private Gui.FillerItem fillerItem = new Gui.FillerItem();

            /**
             * Filler item arguments class
             */
            @Getter @Setter
            public static class FillerItem extends OkaeriConfig {

                /**
                 * status of filler
                 */
                @Comment("Fills empty areas")
                private boolean useFiller = true;

                /**
                 * material of item
                 */
                private String material = "GRAY_STAINED_GLASS_PANE";
            }

            /**
             * PreviousPage item object
             */
            private Gui.PreviousPage previousPage = new Gui.PreviousPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class PreviousPage extends OkaeriConfig {
                /**
                 * material of item
                 */
                private String item = "ARROW";
            }

            /**
             * PreviousPage item object
             */
            private Gui.NextPage nextPage = new Gui.NextPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class NextPage extends OkaeriConfig {
                /**
                 * material of item
                 */
                private String item = "ARROW";
            }

            /**
             * Default Category attributes
             */
            private Gui.DefaultCategory defaultCategory = new Gui.DefaultCategory();

            /**
             * Default Category arguments class
             */
            @Getter @Setter
            public static class DefaultCategory extends OkaeriConfig {

                /**
                 * Default material
                 */
                private String material = "CHEST";
            }

            /**
             * DefaultProduct attributes
             */
            private Gui.DefaultProduct defaultProduct = new Gui.DefaultProduct();

            /**
             * DefaultProduct arguments class
             */
            @Getter @Setter
            public static class DefaultProduct extends OkaeriConfig {

                /**
                 * Default material
                 */
                private String material = "DIAMOND";
            }
        }
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
        private boolean status = false;
        /**
         * Server id of bazaar
         */
        private int serverId = 1;

        /**
         * default storage size
         */
        private int defaultStorageSize = 5;

        /**
         * Gui object
         */
        private Modules.Bazaar.Gui gui = new Bazaar.Gui();

        /**
         * Gui arguments class
         */
        @Getter
        @Setter
        public static class Gui extends OkaeriConfig {

            /**
             * layout of gui
             */
            @Comment({
                    "Layout of gui",
                    "a: Add Item Button",
                    "i: Item",
                    "b: Prev Page",
                    "n: Next Page"
            })
            private List<String> layout = Arrays.asList(
                    "    a    ",
                    " iiiiiii ",
                    "b       n"
            );

            /**
             * Add item material
             */
            private String addItemMaterial = "GREEN_WOOL";

            /**
             * layout of gui
             */
            @Comment({
                    "Layout of gui",
                    "i: Item"
            })
            private List<String> addItemLayout = Arrays.asList(
                    "iiiiiiiii"
            );

            /**
             * Filler item object
             */
            private Modules.Bazaar.Gui.FillerItem fillerItem = new Modules.Bazaar.Gui.FillerItem();

            /**
             * Filler item arguments class
             */
            @Getter @Setter
            public static class FillerItem extends OkaeriConfig {
                /**
                 * status of filler
                 */
                @Comment("Fills empty areas")
                private boolean useFiller = true;

                /**
                 * material of item
                 */
                private String material = "GRAY_STAINED_GLASS_PANE";
            }

            /**
             * PreviousPage item object
             */
            private Modules.Bazaar.Gui.PreviousPage previousPage = new Modules.Bazaar.Gui.PreviousPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class PreviousPage extends OkaeriConfig {
                /**
                 * material of item
                 */
                private String item = "ARROW";
            }

            /**
             * PreviousPage item object
             */
            private Modules.Bazaar.Gui.NextPage nextPage = new Modules.Bazaar.Gui.NextPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class NextPage extends OkaeriConfig {
                /**
                 * material of item
                 */
                private String item = "ARROW";
            }
        }
    }

    /**
     * Donators module setting
     */
    private Donations Donations = new Donations();

    /**
     * Donators module settings
     *
     * @since 1.0
     * @author poyrazinan
     */
    @Getter
    @Setter
    public static class Donations extends OkaeriConfig {
        /**
         * Status of Donators mode
         */
        private boolean status = true;
        /**
         * Donators scheduler second
         */
        private long updateSecond = 900;

        /**
         * Gui object
         */
        private Modules.Donations.Gui gui = new Donations.Gui();

        /**
         * Gui arguments class
         */
        @Getter
        @Setter
        public static class Gui extends OkaeriConfig {

            /**
             * donations gui layout
             */
            @Comment({
                    "Layout of gui",
                    "1: Latest Donations Description",
                    "l: Latest Donations",
                    "2: Top Donations Info (All Time)",
                    "a: Top Donations (All Time)",
                    "3: Top Donations Info (Annual)",
                    "y: Top Donations (Annual)",
                    "4: Top Donations Info (Monthly)",
                    "m: Top Donations (Monthly)",
                    "5: Top Donations Info (Daily)",
                    "d: Top Donations (Daily)",
                    "b: Prev Page",
                    "n: Next Page"
            })
            private List<String> layout = Arrays.asList(
                    " 1 lllll ",
                    " 2 aaaaa ",
                    " 3 yyyyy ",
                    " 4 mmmmm ",
                    " 5 ddddd ",
                    "b       n"
            );

            /**
             * Info Items
             */
            private Modules.Donations.Gui.InfoItems infoItems = new Modules.Donations.Gui.InfoItems();

            /**
             * Filler item arguments class
             */
            @Getter @Setter
            public static class InfoItems extends OkaeriConfig {

                /**
                 * Latest Donations Material of item
                 */
                private String latestMaterial = "PAPER";

                /**
                 * Top Donations (All Time) Material of item
                 */
                private String topAllTimeMaterial = "PAPER";

                /**
                 * Top Donations (Annual) Material of item
                 */
                private String topAnnualMaterial = "PAPER";

                /**
                 * Top Donations (Monthly) Material of item
                 */
                private String topMonthlyMaterial = "PAPER";

                /**
                 * Top Donations (Daily) Material of item
                 */
                private String topDailyMaterial = "PAPER";
            }

            /**
             * Filler item object
             */
            private Modules.Donations.Gui.FillerItem fillerItem = new Modules.Donations.Gui.FillerItem();

            /**
             * Filler item arguments class
             */
            @Getter @Setter
            public static class FillerItem extends OkaeriConfig {

                /**
                 * status of filler
                 */
                @Comment("Fills empty areas")
                private boolean useFiller = true;

                /**
                 * material of item
                 */
                private String material = "GRAY_STAINED_GLASS_PANE";
            }

            /**
             * PreviousPage item object
             */
            private Modules.Donations.Gui.PreviousPage previousPage = new Modules.Donations.Gui.PreviousPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class PreviousPage extends OkaeriConfig {
                /**
                 * material of item
                 */
                private String item = "ARROW";
            }

            /**
             * PreviousPage item object
             */
            private Modules.Donations.Gui.NextPage nextPage = new Modules.Donations.Gui.NextPage();

            /**
             * PreviousPage item arguments class
             */
            @Getter @Setter
            public static class NextPage extends OkaeriConfig {
                /**
                 * material of item
                 */
                private String item = "ARROW";
            }
        }
    }
}