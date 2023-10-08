package net.leaderos.plugin.modules;

import org.jetbrains.annotations.NotNull;

/**
 * Interface Module class
 *
 * @author poyrazinan
 * @since 1.0
 */
public interface Modulable {

    /**
     * Returns a value indicating whether this module is currently
     * enabled
     *
     * @return true if this module is enabled, otherwise false
     */
    boolean isEnabled();

    /**
     * Sets enabled status
     */
    void setEnabled(boolean isEnabled);

    /**
     * Gets status of module in module.yml
     * <p>Enables or disables according to status.</p>
     * @return true if the module enabled, otherwise false
     */
    boolean getStatus();

    /**
     * Returns the name of the plugin.
     * <p>
     * This should return the bare name of the plugin and should be used for
     * comparison.
     *
     * @return name of the plugin
     */
    @NotNull
    String getName();

    /**
     * Called when this plugin is enabled
     */
    void onEnable();

    /**
     * Called when this plugin is disabled
     */
    void onDisable();
}
