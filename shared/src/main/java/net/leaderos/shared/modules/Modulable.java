package net.leaderos.shared.modules;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface Module class
 *
 * @author poyrazinan
 * @since 1.0
 */
public interface Modulable {

    /**
     * Gets dependency list as string data for placeholders or messages.
     *
     * @return String of dependency list.
     */
    String getDependencyListAsString();

    /**
     * Add dependency to module.
     * <p>If the module works with another module, you can add it with this method.</p>
     * @param dependency should be a module and name of it.
     */
    void addDependency(String dependency);

    /**
     * Returns dependency tree of module.
     * If there is any module to work with, the module require it for start.
     *
     * @return List of dependency list. (Module Names)
     */
    List<String> getDependencies();

    /**
     * Returns a value indicating whether this module is currently
     * enabled
     *
     * @return true if this module is enabled, otherwise false
     */
    boolean isEnabled();

    /**
     * Sets enabled status
     * @param isEnabled boolean status of enabled or not
     */
    void setEnabled(boolean isEnabled);

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
     * Called when this plugin on enabled
     */
    void onEnable();

    /**
     * Called when this plugin on disabled
     */
    void onDisable();

    /**
     * Called when this plugin on reloads
     */
    void onReload();
}
