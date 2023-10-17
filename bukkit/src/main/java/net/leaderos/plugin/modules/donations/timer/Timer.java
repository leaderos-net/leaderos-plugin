package net.leaderos.plugin.modules.donations.timer;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.donations.model.RecentDonationData;
import org.bukkit.scheduler.BukkitRunnable;

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
                RecentDonationData.updateAllRecentDonationsData();
            }

        };
        taskid.runTaskTimerAsynchronously(Main.getInstance(),
                1L,
                20*Main.getInstance().getModulesFile().getDonations().getUpdateSecond());

    }
}
