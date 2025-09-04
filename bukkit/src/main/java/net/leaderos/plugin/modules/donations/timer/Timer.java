package net.leaderos.plugin.modules.donations.timer;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.donations.managers.DonationManager;

/**
 * Time checker for update scheduler
 *
 * @author poyrazinan
 * @since 1.0
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

        task = Bukkit.getFoliaLib().getScheduler().runTimerAsync(() -> {
            DonationManager.updateAllData();
        }, 1L, 20* Bukkit.getInstance().getModulesFile().getDonations().getUpdateSecond());
    }
}
