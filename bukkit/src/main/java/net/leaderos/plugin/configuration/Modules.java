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
        private String serverToken = "abc";
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
            @Comment("Layout of gui")
            private List<String> landingGuiLayout = Arrays.asList(
                    "         ",
                    "   ccc   ",
                    "b       n"
            );

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
        private boolean status = true;
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
            @Comment("Layout of gui")
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
            @Comment("Layout of gui")
            private List<String> addItemLayout = Arrays.asList(
                    "iiiiiiiii",
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
        private boolean status = false;
        /**
         * Donators scheduler second
         */
        private long updateSecond = 900;

        /**
         * Limit of recent donations
         */
        private int recentDonationLimit = 5;

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
            @Comment("Layout of gui")
            private List<String> layout = Arrays.asList(
                    "         ",
                    "  ddddd  ",
                    "b       n"
            );

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