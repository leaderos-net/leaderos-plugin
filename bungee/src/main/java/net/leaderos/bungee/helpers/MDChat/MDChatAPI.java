package net.leaderos.bungee.helpers.MDChat;

import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class MDChatAPI {

    /**
     * Send modern messages (Hoverable, linked messages)
     *
     * @param message string
     * @return TextComponent formatted content
     */
    public static @NotNull TextComponent getFormattedMessage(String message) {
        return MDChat.getMessageFromString(message, true);
    }
}