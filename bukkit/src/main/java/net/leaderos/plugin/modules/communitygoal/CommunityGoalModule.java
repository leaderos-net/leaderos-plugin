package net.leaderos.plugin.modules.communitygoal;

import net.leaderos.plugin.modules.communitygoal.timer.Timer;
import net.leaderos.shared.modules.LeaderOSModule;

/**
 * CommunityGoal module main class
 * @author leaderos
 * @since 1.0
 */
public class CommunityGoalModule extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Timer.run();
        if( org.bukkit.Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().register();
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        if (Timer.task != null) {
            Timer.task.cancel();
        }
        if( org.bukkit.Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            new Placeholders().unregister();
    }
}
