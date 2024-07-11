package net.leaderos.plugin.modules.connect.timer;

import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.modules.connect.ConnectModule;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Time checker for update scheduler
 *
 */
public class Timer {

    /**
     * Runnable id for cancel or resume
     */
    public static BukkitRunnable taskid;
    public static void run() {
        if (taskid != null) {
            taskid.cancel();
            taskid = null;
        }
        taskid = new BukkitRunnable() {
            @Override
            public synchronized void cancel() throws IllegalStateException {
                super.cancel();
            }
            public void run() {
                ConnectModule module = (ConnectModule) ModuleManager.getModule("Connect");
                module.reconnect();
            }

        };

        if (Bukkit.getInstance().getModulesFile().getConnect().getReconnectionTimer() > 0) {
            taskid.runTaskTimer(Bukkit.getInstance(),
                    20 * Bukkit.getInstance().getModulesFile().getConnect().getReconnectionTimer(),
                    20 * Bukkit.getInstance().getModulesFile().getConnect().getReconnectionTimer());
        }
    }
}
