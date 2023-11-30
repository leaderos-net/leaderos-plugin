package net.leaderos.velocity.helpers;

import net.leaderos.shared.helpers.DebugAPI;
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
     */
    @Override
    public void send(String message) {
        if (Velocity.getInstance().getConfigFile().getSettings().isDebug()) {
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
