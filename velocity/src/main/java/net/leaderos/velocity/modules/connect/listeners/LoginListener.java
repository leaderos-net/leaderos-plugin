package net.leaderos.velocity.modules.connect.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.leaderos.shared.helpers.Placeholder;
import net.leaderos.velocity.Velocity;
import net.leaderos.velocity.helpers.ChatUtil;
import net.leaderos.velocity.modules.connect.ConnectModule;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginListener {
    @Subscribe
    public void onPlayerJoin(LoginEvent event) {
        Player player = event.getPlayer();

        ConnectModule.getCommandsQueue().getExecutor().execute(() -> {
            try {
                final List<String> commands = ConnectModule.getCommandsQueue().getCommands(player.getUsername());

                if (commands == null || commands.isEmpty()) return;

                Velocity.getInstance().getServer().getScheduler().buildTask(Velocity.getInstance(), () -> {
                    if (!player.isActive()) return;

                    // Execute commands
                    commands.forEach(command -> {
                        Velocity.getInstance().getCommandManager().executeImmediatelyAsync(Velocity.getInstance().getServer().getConsoleCommandSource(), command);

                        Component msg = ChatUtil.replacePlaceholders(Velocity.getInstance().getLangFile().getMessages().getConnect().getConnectExecutedCommandFromQueue(),
                                new Placeholder("%command%", command));
                        ChatUtil.sendMessage(Velocity.getInstance().getServer().getConsoleCommandSource(), msg);
                    });

                    // Remove commands from queue
                    ConnectModule.getCommandsQueue().getExecutor().execute(() -> {
                        ConnectModule.getCommandsQueue().removeCommands(player.getUsername());
                    });
                }).delay(Velocity.getInstance().getModulesFile().getConnect().getExecuteDelay(), TimeUnit.SECONDS).schedule();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
