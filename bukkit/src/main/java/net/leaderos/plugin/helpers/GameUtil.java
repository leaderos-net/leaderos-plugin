package net.leaderos.plugin.helpers;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class GameUtil extends net.leaderos.shared.helpers.GameUtil {

    /**
     * Calculates amount according to permission like (bazaar.maxstorage.10) returns 10
     *
     * @param player to be calculated
     * @param perm to be calculated (example: "bazaar.maxstorage.")
     * @param defaultValue default value if player dont't have permission (example: 3)
     * @return int value of amount
     */
    public static int getAmountFromPerm(Player player, String perm, int defaultValue) {
        try {
            if (player == null)
                return defaultValue;
            else {
                List<Integer> lists = new ArrayList<>();
                for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
                    if (attachmentInfo.getPermission().startsWith(perm))
                        lists.add(Integer.parseInt(attachmentInfo.getPermission().substring(attachmentInfo.getPermission().lastIndexOf(".") +1)));
                }
                if (!lists.isEmpty())
                    defaultValue = lists.stream()
                            .filter(net.leaderos.shared.helpers.GameUtil::isInteger)
                            .reduce(1, Integer::max);
            }
            return defaultValue;
        }

        catch(Exception e1) {
            return defaultValue;
        }
    }
}
