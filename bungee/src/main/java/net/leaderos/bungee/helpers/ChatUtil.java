package net.leaderos.bungee.helpers;

import net.leaderos.bungee.Bungee;
import net.leaderos.shared.helpers.Placeholder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hyperion
 * @since 1.0
 */
public class ChatUtil {

    /**
     * Decimal formatter
     */
    public static final DecimalFormat FORMATTER = (DecimalFormat) NumberFormat.getNumberInstance();

    static {
        FORMATTER.setMinimumIntegerDigits(1);
        FORMATTER.setMaximumIntegerDigits(20);
        FORMATTER.setMaximumFractionDigits(2);
        FORMATTER.setGroupingSize(3);
    }

    /**
     * Hex pattern for color codes
     */
    private final static Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    /**
     * Applies chat color formats to message
     * @param message to convert
     * @return String of converted message
     */
    public static String color(String message) {
        message = StringEscapeUtils.unescapeHtml4(message);

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of(matcher.group()).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    /**
     * Applies chat color formats to list
     * @param list to convert
     * @return List of converted message
     */
    public static List<String> color(List<String> list) {
        return list.stream().map(ChatUtil::color).collect(Collectors.toList());
    }

    /**
     * Get colored message with prefix
     * @param message to send
     * @return colored message
     */
    public static String getMessage(String message) {
        return ChatUtil.color(replacePlaceholders(message, new Placeholder("{prefix}",
                Bungee.getInstance().getLangFile().getMessages().getPrefix())));
    }

    /**
     * Sends message to command sender
     * @param player executor
     * @param message to send
     */
    public static void sendMessage(@NotNull CommandSender player, String message) {
        player.sendMessage(ChatUtil.color(replacePlaceholders(message, new Placeholder("{prefix}",
                Bungee.getInstance().getLangFile().getMessages().getPrefix()))));
    }

    /**
     * Replaces placeholder data on string
     * <p><b>also format chat messages too @see ChatUtil#color(String)</b></p>
     *
     * @param string to be converted
     * @param placeholders additional placeholder data
     * @return converted string value
     */
    public static String replacePlaceholders(String string, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            string = string.replace(placeholder.getKey(), placeholder.getValue());
        }
        return color(string);
    }

    /**
     * Replaces placeholder data on list
     *
     * @param list to be converted
     * @param placeholders additional placeholder data
     * @return converted list value
     */
    public static List<String> replacePlaceholders(List<String> list, Placeholder... placeholders) {
        return list.stream().map(s-> replacePlaceholders(s, placeholders)).collect(Collectors.toList());
    }
}
