package net.leaderos.plugin.helpers;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.leaderos.plugin.Bukkit;
import net.leaderos.shared.helpers.Placeholder;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hyperion, poyrazinan
 * @since 1.0
 */
public class ChatUtil {

    public static final DecimalFormat FORMATTER = (DecimalFormat) NumberFormat.getNumberInstance();

    static {
        FORMATTER.setMinimumIntegerDigits(1);
        FORMATTER.setMaximumIntegerDigits(20);
        FORMATTER.setMaximumFractionDigits(2);
        FORMATTER.setGroupingSize(3);
    }

    private final static Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    /**
     * Simple tag pattern to detect MiniMessage format.
     * E.g.: <red>, <#ff0000>, <gradient:red:blue>, <hover:...>
     */
    private final static Pattern MINIMESSAGE_PATTERN = Pattern.compile("<[a-zA-Z0-9_#:,!/ ]+>");

    /**
     * MiniMessage instance (thread-safe, no need to recreate repeatedly)
     */
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /**
     * Component -> legacy (§ coded) String serializer.
     * Also supports Hex colors.
     */
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .character('§')
            .hexColors()
            .build();

    /**
     * Applies chat color formats to message.
     * Supports both legacy (&, hex) and MiniMessage formats.
     * @param message to convert
     * @return String of converted message
     */
    public static String color(String message) {
        message = StringEscapeUtils.unescapeHtml4(message);

        // Process MiniMessage format first if it contains it
        if (containsMiniMessageTags(message)) {
            try {
                Component component = MINI_MESSAGE.deserialize(message);
                return LEGACY_SERIALIZER.serialize(component);
            } catch (Exception ignored) {
                // If there is an invalid/unparseable MiniMessage tag, continue with the legacy system
            }
        }

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of(matcher.group()).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    /**
     * Checks if the message contains MiniMessage tags
     * @param message message to check
     * @return true if it contains MiniMessage tags
     */
    private static boolean containsMiniMessageTags(String message) {
        return MINIMESSAGE_PATTERN.matcher(message).find();
    }

    /**
     * Removes color codes from text
     * @param text to remove color codes
     * @return String of removed color codes
     */
    public static String removeColorCode(String text) {
        String regex = "(&[a-zA-Z0-9]|§[a-zA-Z0-9]|#[0-9a-fA-F]{6})";
        text = text.replaceAll(regex, "");
        return text;
    }

    public static List<String> color(List<String> list) {
        return list.stream().map(ChatUtil::color).collect(Collectors.toList());
    }

    public static String getMessage(String message) {
        return ChatUtil.color(replacePlaceholders(message, new Placeholder("{prefix}",
                Bukkit.getInstance().getLangFile().getMessages().getPrefix())));
    }

    public static void sendMessage(@NotNull CommandSender player, String message) {
        player.sendMessage(ChatUtil.color(replacePlaceholders(message, new Placeholder("{prefix}",
                Bukkit.getInstance().getLangFile().getMessages().getPrefix()))));
    }

    public static String replacePlaceholders(String string, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            string = string.replace(placeholder.getKey(), placeholder.getValue());
        }
        return color(string);
    }

    public static String replacePlaceholders(String string) {
        if (org.bukkit.Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            string = PlaceholderAPI.setPlaceholders(null, string);

        return color(string);
    }

    public static List<String> replacePlaceholders(List<String> list, Placeholder... placeholders) {
        return list.stream().map(s-> replacePlaceholders(s, placeholders)).collect(Collectors.toList());
    }
}