package net.leaderos.velocity.helpers;

import net.leaderos.shared.helpers.DebugAPI;
import net.leaderos.shared.model.DebugMode;
import net.leaderos.velocity.Velocity;

/**
 * Sends debug to console
 * @author poyrazinan
 * @since 1.0.2
 */
public class DebugVelocity implements DebugAPI {

    /**
     * Sends debug to console
     * @param message to debug
     * @param strict if true, it will send debug even if debug mode is disabled
     */
    @Override
    public void send(String message, boolean strict) {
        if (
                Velocity.getInstance().getConfigFile().getSettings().getDebugMode() == DebugMode.ENABLED ||
                        (Velocity.getInstance().getConfigFile().getSettings().getDebugMode() == DebugMode.ONLY_ERRORS && strict)
        ) {
            Velocity.getInstance().getLogger().warn(
                    "[DEBUG] " + message
            );
        }
    }

    /**
     * Constructor of debug
     */
    public DebugVelocity() {}
}
