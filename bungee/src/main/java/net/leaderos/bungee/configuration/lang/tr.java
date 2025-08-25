package net.leaderos.bungee.configuration.lang;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.bungee.configuration.Language;

import java.util.Arrays;
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

        private String haveRequestOngoing = "&cLütfen şu anki işleminin bitmesini bekle!";

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
}
