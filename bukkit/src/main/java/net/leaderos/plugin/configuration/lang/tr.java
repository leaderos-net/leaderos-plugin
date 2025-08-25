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

        @Comment("Prefix of messages")
        private String prefix = "&3LeaderOS &8»";

        private String reload = "{prefix} &aEklenti başarıyla yeniden yüklendi.";

        private String update = "{prefix} &eLeaderOS plugini için yeni bir güncelleme mevcut! Lütfen &a%version% &eversiyonuna güncelleyin!";

        private String changeApiUrl = "{prefix} &cLütfen API URL adresini değiştirin!";

        private String changeApiUrlHttps = "{prefix} &cAPI URL adresi HTTPS (https://) ile başlamalıdır!";

        private String playerNotOnline = "{prefix} &cOyuncu çevrimiçi değil.";

        private String playerNotAvailable = "{prefix} &cOyuncu mevcut değil.";

        private String targetPlayerNotAvailable = "{prefix} &cBu oyuncu mevcut değil.";

        private String cannotCreateFull = "{prefix} &cEnvanterinde yeterli boşluk bulunmamakta, lütfen envanterini boşalt ve tekrar dene.";

        private String haveRequestOngoing = "&cLütfen şu anki işleminin bitmesini bekleyiniz.";

        private String registrationRequired = "{prefix} &cBu işlemi yapabilmek için siteye kayıt olmalısın!";

        /**
         * Help commands message
         */
        @Comment("Help commands message")
        private List<String> help = Arrays.asList(
                "&6&l  LEADEROS EKLENTI KOMUTLARI",
                "",
                "&8 ▪ &e/ai <prompt> &8» &fYapay Zekaya soru sorar.",
                "&8 ▪ &e/verify <kod> &8» &fMinecraft hesabınızı doğrular.",
                "&8 ▪ &e/discord-sync &8» &fDiscord eşleme bağlantısı verir.",
                "",
                "&8 ▪ &e/webbazaar &8» &fPazar depo menüsünü açar.",
                "&8 ▪ &e/webstore &8» &fMağaza menüsünü açar.",
                "&8 ▪ &e/donations &8» &fBağışlar menüsünü açar.",
                "",
                "&8 ▪ &e/creditsvoucher give <oyuncu> <miktar> &8» &fKredi kağıdı verir.",
                "&8 ▪ &e/creditsvoucher create <miktar> &8» &fKredi kağıdı oluşturur.",
                "",
                "&8 ▪ &e/credits &8» &fKredi miktarını görüntüler.",
                "&8 ▪ &e/credits see <oyuncu> &8» &fOyuncunun kredi miktarını görüntüler.",
                "&8 ▪ &e/credits send <oyuncu> <miktar> &8» &fOyuncuya kredi gönderir.",
                "&8 ▪ &e/credits set <oyuncu> <miktar> &8» &fOyuncunun kredisini ayarlar.",
                "&8 ▪ &e/credits remove <oyuncu> <miktar> &8» &fOyuncunun kredisini siler.",
                "&8 ▪ &e/credits add <oyuncu> <miktar> &8» &fOyuncuya kredi ekler.",
                "",
                "&8 ▪ &e/leaderos reload &8» &fEklenti dosyalarını yeniler."
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
            private String moduleEnabled = "{prefix} &a%module_name% modülü aktif edildi.";

            /**
             * Module closed message
             */
            private String moduleClosed = "{prefix} &c%module_name% modülü kapandı.";

            /**
             * Module disabled message
             */
            private String moduleDisabled = "{prefix} &c%module_name% modülü devre dışı bırakıldı.";

            /**
             * Missing dependency message
             */
            private String missingDependency = "{prefix} &c%module_name% modülü gereksinimleri karşılayamadığı için başlatılamadı. Gereksinim: &8[&c%dependencies%&8]";
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
            private String invalidArgument = "{prefix} &cBilinmeyen argüman girdiniz!";

            /**
             * Unknown command message
             */
            private String unknownCommand = "{prefix} &cBilinmeyen komut girdiniz!";

            /**
             * Not enough arguments message
             */
            private String notEnoughArguments = "{prefix} &cYetersiz argüman girdiniz!";

            /**
             * too many arguments message
             */
            private String tooManyArguments = "{prefix} &cÇok fazla argüman girdiniz!";

            /**
             * no perm message
             */
            private String noPerm = "{prefix} &cBu işlemi yapabilmek için yeterli yetkiye sahip değilsin!";

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
            private String successMessage = "{prefix} &aHesabınız başarıyla doğrulandı. Websiteye geri dönünüz.";

            /**
             * error message
             */
            private String failMessage = "{prefix} &cHesabınız doğrulanamadı. Lütfen daha sonra tekrar deneyin.";
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
            private String usage = "{prefix} &fKullanım: &e/ai <soru>";

            /*
             * Generating AI response message
             */
            private String generating = "{prefix} &7Yapay Zeka cevap oluşturuyor, lütfen bekleyiniz...";

            /**
             * Error message
             */
            private String failMessage = "{prefix} &cYapay Zeka ile iletişim kurulamadı. Lütfen daha sonra tekrar deneyin.";
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
            private String commandMessage = "{prefix} <&aHesabını Discord ile eşlemek için tıkla!{&eTıkla!}(open_url:%link%)>";

            /**
             * error on DiscordSync link
             */
            private String noLink = "{prefix} &cSunucuya bağlanırken hata oluştu.";
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
                    "Kredi kağıdı eşya ismi",
                    "Kredi kağıdı tutarı için {amount} kullanabilirsiniz"
            })
            private String itemDisplayName = "&8[&a{amount} kredi&8] &7#{id}";

            @Comment("Kredi kağıdı eşya açıklaması")
            private List<String> itemLore = Collections.singletonList("&7Kağıdı kullanmak için sağ tıklayın.");

            @Comment({
                    "Kullanıcı kredi kağıdını tekrar kullanmaya çalıştığındaki hata mesajı.",
                    "Bu kopyalama açığını engeller."
            })
            private String alreadyUsed = "{prefix} &cBu kredi kağıdı zaten kullanılmış.";

            private String successfullyUsed = "{prefix} &aKredi kağıdı başarıyla kullanıldı. &e{amount} kredi &ahesabına eklendi.";

            private String successfullyCreated = "{prefix} &e{amount} kredi &adeğerindeki kredi kağıdı başarıyla oluşturuldu.";

            private String cannotCreateNegative = "{prefix} &cLütfen geçerli bir miktar giriniz.";

            private String cannotCreateNotEnough = "{prefix} &cYeterli miktarda krediye sahip değilsin. Gereken: &e{amount} kredi";

            private String successfullyGave = "{prefix} &b{target} &aadlı oyuncuya &e{amount} kredi &adeğerindeki kredi kağıdı verildi.";

            private String helpStaff = "{prefix} &7/vouchers give <oyuncu> <miktar>";

            private String help = "{prefix} &7/vouchers create <miktar>";

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

            private String creditInfo = "{prefix} &e{amount} &akrediye sahipsin.";

            private String creditInfoOther = "{prefix} &b{target} &aadlı oyuncunun &e{amount} &akredisi bulunuyor.";

            private String cannotSendCreditsThisUser = "{prefix} &cBu kullanıcıya kredi gönderemezsin.";

            private String cannotSendCreditYourself = "{prefix} &cKendine kredi gönderemezsin.";

            private String cannotSendCreditNegative = "{prefix} &cLütfen geçerli bir miktar giriniz.";

            private String cannotSendCreditNotEnough = "{prefix} &cYeterli miktarda krediye sahip değilsin. Gereken: &e{amount} kredi";

            private String successfullySentCredit = "{prefix} &b{target} &aadlı oyuncuya başarıyla &e{amount} kredi &agönderildi.";

            private String successfullySetCredit = "{prefix} &b{target} &aadlı oyuncunun kredisi başarıyla &e{amount} olarak ayarlandı.";

            private String successfullyAddedCredit = "{prefix} &b{target} &aadlı oyuncuya başarıyla &e{amount} kredi &aeklendi.";

            private String successfullyRemovedCredit = "{prefix} &b{target} &aadlı oyuncunun &e{amount} kredisi &abaşarıyla silindi.";

            private String receivedCredit = "{prefix} &b{player} &aadlı oyuncudan &e{amount} kredi &aaldın.";

            private String cacheUpdated = "{prefix} &b{target} &aadlı oyuncunun önbelleğe alınmış verisi güncellendi.";
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

            private String connectExecutedCommand = "{prefix} &aConnect modülü tarafından komut çalıştırıldı: &e%command%";

            private String connectWillExecuteCommand = "{prefix} &aConnect modülü, oyuncu sunucuya katıldıktan sonra komutu çalıştıracak: &e%command%";

            private String connectExecutedCommandFromQueue = "{prefix} &aConnect modülü, oyuncu sunucuya katıldıktan sonra komutu çalıştırdı: &e%command%";

            private String subscribedChannel = "{prefix} &aConnect modülü başarıyla sunucuya bağlandı!";

            private String commandBlacklisted = "{prefix} &cBu komut kara listeye alındığı için çalıştırılamadı: &e%command%";
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
                private String name = "&eÖnceki Sayfa";
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
                private String name = "&eSonraki Sayfa";
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
            private String guiName = "&8Site Market";

            /**
             * Price format
             */
            @Comment({
                    "Fiyat: {price} (örn. 10.00 TL)",
                    "Ham Fiyat: {rawPrice} (örn. 10.00)"
            })
            private String priceFormat = "{price}";

            /**
             * Discounted price format
             */
            @Comment({
                    "İndirimsiz Fiyat: {price} (örn. 10.00 TL)",
                    "Ham İndirimsiz Fiyat: {rawPrice} (örn. 10.00)",
                    "İndirimli Fiyat: {discountedPrice} (örn. 5.00 TL)",
                    "Ham İndirimli Fiyat: {rawDiscountedPrice} (örn. 5.00)",
            })
            private String discountedPriceFormat = "&c&m{price}&r &a{discountedPrice}";

            /**
             * Discount amount format
             */
            private String discountAmountFormat = "&8[&a%{discount}&8]";

            /**
             * Stock unlimited format
             */
            private String stockUnlimited = "&6&lLIMITSIZ";

            /**
             * purchase title
             */
            private String buyWebStoreTitle = "&6&lSATIN AL";

            /**
             * purchase subtitle progress
             */
            private String buyWebStoreProgress = "&7Satın alım devam ediyor...";

            /**
             * purchase subtitle success
             */
            private String buyWebStoreSuccess = "&aSatın alım başarılı.";

            private String buyWebStoreOutOfStock = "&cStok yok.";

            private String buyWebStoreProductNotFound = "&cÜrün bulunamadı.";

            private String buyWebStoreUserNotFound = "&cKullanıcı bulunamadı.";

            private String webStoreCategoryNotFound = "&cKategori bulunamadı.";

            private String buyWebStoreRequiredProduct = "&cSahip olmadığınız gerekli ürünleri satın almalısınız!";

            private String buyWebStoreRequiredLinkedAccount = "&cBu ürünü satın almak için hesabınızı eşlemeniz gerekiyor!";

            private String buyWebStoreDowngradeNotAllowed = "&cAboneliğinizin paketini düşüremezsiniz!";

            private String buyWebStoreInvalidVariable = "&cGeçersiz değişken girdiniz!";

            /**
             * purchase subtitle success
             */
            private String buyWebStoreNotEnoughCredit = "&cYetersiz kredi.";

            /**
             * purchase subtitle error
             */
            private String buyWebStoreError = "&cSatın alım hatası.";

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
                        "&aTıkla ve kategoriyi aç!"
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
                        "&dÜrün Detayları:",
                        "&8 ▪ &7Fiyat &8» &e%price%",
                        "&8 ▪ &7Stok &8» &e%stock%",
                        "",
                        "&aTıkla ve satın al!"
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
                private String title = "&eKredi: &a%credits%";
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
                private String guiName = "&8Satın Alımı Onayla";

                /**
                 * Confirm Title
                 */
                private String confirmTitle = "&a&lSATIN AL";

                /**
                 * Cancel Title
                 */
                private String cancelTitle = "&c&lIPTAL ET";
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
            private String guiName = "&8Pazar";

            /**
             * lore modifier
             */
            private String clickLore = "&aTıkla ve geri al!";

            /**
             * withdraw item title
             */
            private String withdrawTitle = "&6&lGERI ALIM";

            /**
             * withdraw item subtitle progress
             */
            private String withdrawProgressSubtitle = "&7Geri alım devam ediyor...";

            /**
             * withdraw item subtitle success
             */
            private String withdrawSuccessSubtitle = "&aGeri alım başarılı.";

            /**
             * withdraw item subtitle error
             */
            private String withdrawErrorSubtitle = "&cGeri alım hatası.";

            /**
             * Add item title
             */
            private String addItemName = "&eEşya Ekle";

            /**
             * Add item lore
             */
            private List<String> addItemLore = Arrays.asList("", "&aTıkla ve eşya ekle!");

            /**
             * Add item gui name
             */
            private String addItemGuiName = "&8Pazar » Eşya Ekle";

            private String addItemMessage = "{prefix} &e%item_name% &aeşyası pazar deposuna eklendi.";

            /**
             * return item message
             */
            private String returnItemMessage = "{prefix} &cDepo sınırına ulaştın, en fazla &8(&4%max_amount%&8) &ceşya ekleyebilirsin. &e%amount% &cadet eşya iade edildi.";
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
            private String guiName = "&8Bağışlar";

            /**
             * name of donator item
             */
            private String displayName = "&e#%i% &6%player%";

            /**
             * updates donation data
             */
            private String updatedDonationDataMessage = "{prefix} &aBağışçı verileri güncellendi.";

            /**
             * lore of donator item
             */
            private List<String> lore = Arrays.asList(
                    "",
                    "&7Bağış: &e%amount% %symbol%",
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
                    private String displayName = "&eSon Bağışlar";
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
                    private String displayName = "&eEn Çok Bağış Yapanlar (Tüm Zamanlar)";
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
                    private String displayName = "&eEn Çok Bağış Yapanlar (Yıllık)";
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
                    private String displayName = "&eEn Çok Bağış Yapanlar (Aylık)";
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
                    private String displayName = "&eEn Çok Bağış Yapanlar (Günlük)";
                }
            }
        }
    }
}
