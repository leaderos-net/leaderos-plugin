package net.leaderos.plugin.modules.connect.timer;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.modules.connect.ConnectModule;

/**
 * Time checker for update scheduler
 *
 */
public class Timer {

    /**
     * Runnable id for cancel or resume
     */
    public static WrappedTask task;
    public static void run() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        if (Bukkit.getInstance().getModulesFile().getConnect().getReconnectionTimer() > 0) {
            task = Bukkit.getFoliaLib().getScheduler().runTimer(() -> {
                ConnectModule module = (ConnectModule) ModuleManager.getModule("Connect");
                module.reconnect();

            }, 20 * Bukkit.getInstance().getModulesFile().getConnect().getReconnectionTimer(), 20 * Bukkit.getInstance().getModulesFile().getConnect().getReconnectionTimer());
        }
    }
}
