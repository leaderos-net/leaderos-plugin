package net.leaderos.plugin.shared.module;

import lombok.Getter;
import net.leaderos.plugin.Main;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Module and its main class. It contains fundamental methods
 * and fields for a module to be loaded and work properly. This is an indirect
 * implementation of {@link Modulable}.
 * @author poyrazinan
 * @since 1.0
 */
public abstract class LeaderOSModule implements Modulable {

    /**
     * Gets name of module
     * @return String name of module
     */
    @Override
    public @NotNull String getName() {
        return getClass().getSimpleName();
    }

    /**
     * is module enabled or not
     */
    @Getter
    private boolean isEnabled = false;

    /**
     * Sets status of isEnabled
     * @param isEnabled module
     */
    @Override
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * Gets status of plugin
     * @return boolean of status
     */
    @Override
    public boolean getStatus() {
        return Main.getInstance().getModulesFile().getBoolean("modules." + getName() + ".status");
    }

    /**
     * onEnable method of module
     */
    @Override
    public void onEnable() {}

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {}

    /**
     * Constructor of LeaderOSModule
     */
    public LeaderOSModule() {}
}
