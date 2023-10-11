package net.leaderos.shared.module.auth.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.leaderos.shared.module.auth.AuthLogin;
import org.bukkit.entity.Player;

/**
 * Auth commands
 * @author poyrazinan
 * @since 1.0
 */
@RequiredArgsConstructor
@Command(value = "auth", alias = {"authy", "kayit", "site", "web"})
public class AuthCommand extends BaseCommand {

    /**
     * Default command of auth
     * @param player
     */
    @Default
    @Permission("leaderos.auth")
    public void defaultCommand(Player player) {
        AuthLogin.sendAuthCommandMessage(player);
        // TODO Else
    }
}