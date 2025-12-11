package net.leaderos.plugin.modules.communitygoal.timer;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.communitygoal.managers.CommunityGoalManager;

/**
 * Time checker for update scheduler
 *
 * @author leaderos
 * @since 1.0
 */
public class Timer {

    /**
     * Runnable id for cancel or resume
     */
    public static WrappedTask task;

    /**
     * Run the timer
     */
    public static void run() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        task = Bukkit.getFoliaLib().getScheduler().runTimerAsync(() -> {
            CommunityGoalManager.update();
        }, 1L, 20 * Bukkit.getInstance().getModulesFile().getCommunityGoal().getUpdateSecond());
    }
}
