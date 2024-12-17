package net.leaderos.plugin.helpers;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author hyperion, poyrazinan
 * @since 1.0
 */
public class ItemUtil {

    /**
     * convert item to base64
     *
     * @param itemStack itemStack of item
     * @return base64 of item
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
     * @param encoded data
     * @return created item
     */
    public static ItemStack fromBase64(String encoded) {
        try (ByteArrayInputStream outputStream = new ByteArrayInputStream(Base64Coder.decodeLines(encoded));
             BukkitObjectInputStream dataOutput = new BukkitObjectInputStream(outputStream)) {

            return (ItemStack) dataOutput.readObject();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to read itemStack.", e);
        }
    }


    /**
     * Enchantment data to serialize
     * @param itemStack item
     * @return String of serialized data
     */
    public static String getEnchantments(ItemStack itemStack) {
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<Enchantment, Integer>> entries;
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta =(EnchantmentStorageMeta)itemStack.getItemMeta();
            entries = meta.getStoredEnchants().entrySet();
        } else {
            entries = itemStack.getEnchantments().entrySet();
        }
        for (Map.Entry<Enchantment, Integer> entry : entries) {
            builder.append((XMaterial.supports(13) ? entry.getKey().getKey().getKey() : entry.getKey().getName()).toUpperCase())
                    .append(':').append(entry.getValue()).append(',');
        }

        String enchantments = builder.length() > 1 ?
                builder.deleteCharAt(builder.lastIndexOf(",")).toString() : null;
        return enchantments;
    }


    /**
     * Gets modelId of item
     * @param itemStack of item
     * @return String of model data
     */
    public static String getModelId(ItemStack itemStack) {
        String modelId = null;
        if (XMaterial.supports(14) && itemStack.hasItemMeta() && itemStack.getItemMeta().hasCustomModelData())
            modelId = String.valueOf(itemStack.getItemMeta().getCustomModelData());
        return modelId;
    }

    /**
     * Gets durability of item
     * @param item itemstack
     * @param maxDurability max durability of item
     * @return durability as int
     */
    public static int getDurability(ItemStack item, int maxDurability) {
        int durability = 0;
        if (XMaterial.supports(13))
            durability = maxDurability - ((Damageable) item.getItemMeta()).getDamage();
        else
            durability = maxDurability + 1 - item.getDurability();
        return durability;
    }

    /**
     * Check for material and
     * get item with a material.
     * @param material of item
     * @param name of item
     * @param lore of item
     * @return ItemStack of destination item
     */
    public static @NotNull ItemStack getItem(XMaterial material, String name, List<String> lore) {
        ItemStack result;
        // material based item
        result = material.parseItem();
        ItemMeta meta = result.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        result.setItemMeta(meta);
        return result;
    }

    /**
     * Check for material and
     * get item with a material and model id.
     * for WebStore
     * @param material of item
     * @param name of item
     * @param lore of item
     * @return ItemStack of destination item
     */
    public static @NotNull ItemStack getItem(XMaterial material, String name, List<String> lore, int modelId) {
        ItemStack result;
        // material based item
        result = material.parseItem();
        ItemMeta meta = result.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        meta.addItemFlags(XItemFlag.HIDE_ENCHANTS.get());
        meta.addItemFlags(XItemFlag.HIDE_ATTRIBUTES.get());
        meta.addItemFlags(XItemFlag.HIDE_ADDITIONAL_TOOLTIP.get());
        if (XMaterial.supports(14) && modelId > 0) {
            meta.setCustomModelData(modelId);
        }
        result.setItemMeta(meta);
        return result;
    }

    /**
     * Check for material and
     * get item with a material.
     * for Voucher item
     * @param material of item
     * @param name of item
     * @param lore of item
     * @param glow glow status of item
     * @return ItemStack of destination item
     */
    public static @NotNull ItemStack getItem(XMaterial material, String name, List<String> lore, boolean glow) {
        ItemStack result = getItem(material, name, lore);
        ItemMeta meta = result.getItemMeta();
        if (glow) {
            assert XEnchantment.UNBREAKING.get() != null;
            assert meta != null;
            meta.addEnchant(XEnchantment.UNBREAKING.get(), 1, true);
            meta.addItemFlags(XItemFlag.HIDE_ENCHANTS.get());
        }
        result.setItemMeta(meta);
        return result;
    }

    /**
     * Check for material and
     * get item with a material.
     * @param material of item
     * @param name of item
     * @return ItemStack of destination item
     */
    public static @NotNull ItemStack getItem(XMaterial material, String name) {
        ItemStack result;
        // material based item
        result = material.parseItem();
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(name);
        result.setItemMeta(meta);
        return result;
    }

    /**
     * converts xmaterial to string
     * @param item Item
     * @return String of item
     */
    public static String getName(ItemStack item) {
        XMaterial material = XMaterial.matchXMaterial(item);
        String name = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ?
                item.getItemMeta().getDisplayName() :
                WordUtils.capitalize(material.name().replace('_', ' ').toLowerCase(Locale.ENGLISH));;
        return name;
    }
}
