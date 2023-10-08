package net.leaderos.plugin.bukkit.helpers;

import net.leaderos.plugin.Main;
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
 * @author hyperion
 * @since 1.0
 */
public class ChatUtil {

    /**
     * Decimal formatter
     */
    public static final DecimalFormat FORMATTER = (DecimalFormat) NumberFormat.getNumberInstance();

    /**
     * Formatter static body
     */
    static {
        FORMATTER.setMinimumIntegerDigits(1);
        FORMATTER.setMaximumIntegerDigits(20);
        FORMATTER.setMaximumFractionDigits(2);
        FORMATTER.setGroupingSize(3);
    }

    private final static Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    public static void sendMessage(@NotNull CommandSender player, String message) {
        player.sendMessage(color(replacePlaceholders(message, new Placeholder("{prefix}",
                Main.getInstance().getLangFile().getMessages().getPrefix()))));
    }

    public static String color(String message) {
        message = StringEscapeUtils.unescapeHtml4(message);

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of(matcher.group()).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static List<String> color(List<String> list) {
        return list.stream().map(ChatUtil::color).collect(Collectors.toList());
    }

    public static String replacePlaceholders(String string, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            string = string.replace(placeholder.getKey(), placeholder.getValue());
        }

        return color(string);
    }

    public static List<String> replacePlaceholders(List<String> list, Placeholder... placeholders) {
        return list.stream().map(s-> replacePlaceholders(s, placeholders)).collect(Collectors.toList());
    }
}
