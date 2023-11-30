package net.leaderos.velocity.helpers;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.velocity.Velocity;
import org.jetbrains.annotations.NotNull;

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
     * Converts component to string
     * @param component to be converted
     * @return string converted data
     */
    public static String componentToString(Component component) {
        // LegacyComponentSerializer ile Component'i String'e dönüştür
        String serialized = LegacyComponentSerializer.legacyAmpersand().serialize(component);
        return serialized;
    }

    /**
     * Applies chat color formats to message
     * @param message to convert
     * @return String of converted message
     */
    public static Component color(String message) {
        // HEX renk kodlarını dönüştür
        Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher hexMatcher = hexPattern.matcher(message);

        StringBuffer hexResult = new StringBuffer();
        while (hexMatcher.find()) {
            String colorCode = hexMatcher.group();
            TextColor textColor = TextColor.fromHexString(colorCode);
            String replacement = LegacyComponentSerializer.legacyAmpersand().serialize(Component.text("").color(textColor));
            hexMatcher.appendReplacement(hexResult, replacement);
        }
        hexMatcher.appendTail(hexResult);

        // Klasik Minecraft renk kodlarını dönüştür
        String classicColors = hexResult.toString();
        classicColors = classicColors.replaceAll("&([a-fA-F0-9])", "\u00A7$1");

        return Component.text(classicColors);
    }

    /**
     * Applies chat color formats to list
     * @param list to convert
     * @return List of converted message
     */
    public static List<Component> color(List<String> list) {
        return list.stream().map(ChatUtil::color).collect(Collectors.toList());
    }

    /**
     * Get colored message with prefix
     * @param message to send
     */
    public static Component getMessage(String message) {
        return replacePlaceholders(message, new Placeholder("{prefix}",
                Velocity.getInstance().getLangFile().getMessages().getPrefix()));
    }

    /**
     * Sends message to command sender
     * @param target executor
     * @param message to send
     */
    public static void sendMessage(@NotNull CommandSource target, String message) {
        target.sendMessage(replacePlaceholders(message, new Placeholder("{prefix}",
                Velocity.getInstance().getLangFile().getMessages().getPrefix())));
    }

    /**
     * Sends message to command sender
     * @param target executor
     * @param component to send
     */
    public static void sendMessage(@NotNull CommandSource target, Component component) {
        target.sendMessage(replacePlaceholders(componentToString(component), new Placeholder("{prefix}",
                Velocity.getInstance().getLangFile().getMessages().getPrefix())));
    }

    /**
     * Replaces placeholder data on string
     * <p><b>also format chat messages too @see ChatUtil#color(String)</b></p>
     *
     * @param string to be converted
     * @param placeholders additional placeholder data
     * @return converted string value
     */
    public static Component replacePlaceholders(String string, Placeholder... placeholders) {
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
    public static List<Component> replacePlaceholders(List<String> list, Placeholder... placeholders) {
        return list.stream().map(s-> replacePlaceholders(s, placeholders)).collect(Collectors.toList());
    }
}
