package net.leaderos.plugin.bukkit.helpers;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author hyperion
 * @since 1.0
 */
public class ItemUtils {

    /**
     * convert item to base64
     *
     * @param itemStack itemstack of item
     * @return
     */
    public static String toBase64(ItemStack itemStack) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(itemStack);

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save itemStack.", e);
        }
    }

    /**
     * creates item stack by base64
     * @param encoded
     * @return
     */
    public static ItemStack fromBase64(String encoded) {
        try (ByteArrayInputStream outputStream = new ByteArrayInputStream(Base64Coder.decodeLines(encoded));
             BukkitObjectInputStream dataOutput = new BukkitObjectInputStream(outputStream)) {

            return (ItemStack) dataOutput.readObject();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to read itemStack.", e);
        }
    }
}
