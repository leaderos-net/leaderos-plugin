package net.leaderos.plugin.modules.webstore.model;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Products of Categories on WebStore
 */
@Getter
@Setter
public class Product {

    /**
     * Product status
     */
    private boolean status;

    /**
     * Product Id
     */
    private String productId;

    /**
     * Product Display Name
     */
    private String productName;

    /**
     * Product lore
     */
    private List<String> productLore = new ArrayList<>();

    /**
     * Product material
     */
    private XMaterial material;

    /**
     * Product model id
     */
    private int modelId;

    /**
     * Product total price
     */
    private double total;

    /**
     * Product discount
     */
    private double discount;

    /**
     * Product original price
     */
    private double originalPrice;

    /**
     * Product original price text with currency
     */
    private String originalPriceText;

    /**
     * Product total price text with currency
     */
    private String totalText;

    /**
     * Product discount percent
     */
    private int discountPercent;

    /**
     * Product stock
     */
    private int stock;

    /**
     * Restricted Status
     */
    private boolean restricted;

    /**
     * Constructor of Product
     * @param product response data of product
     */
    public Product(@NotNull JSONObject product) {
        // Checks if product active or not
        if (product.getString("isActive").equals("1")
                && product.getString("minecraftStatus").equals("1"))
            this.status = true;
        else {
            this.status = false;
            return;
        }
        this.productId = product.getString("id");

        // if title is empty removes it.
        this.productName = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getDefaultProduct().getTitle().replace("%name%", product.getString("name"));
        try {
            if (product.getString("minecraftTitle") != null && !product.getString("minecraftTitle").isEmpty())
                this.productName = product.getString("minecraftTitle");
        }
        catch (Exception ignored) {}

        try {
            productLore = new ArrayList<>(Arrays.asList(product.getString("minecraftDescription").split("\r\n")));
        }
        catch (Exception ignored) {}
        this.productLore.addAll(Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getDefaultProduct().getLore());

        // price, discountedPrice, stock data
        this.total = product.getDouble("total");
        this.originalPrice = product.getDouble("originalPrice");
        this.discount = product.getDouble("discount");
        this.stock = product.getInt("stock");
        this.restricted = product.getBoolean("restricted");

        this.originalPriceText = product.getString("originalPriceFormatted");
        this.totalText = product.getString("totalFormatted");
        this.discountPercent = product.getInt("discountPercent");

        String materialName = product.getString("minecraftItem");
        if (materialName != null && XMaterial.matchXMaterial(materialName).isPresent())
            this.material = XMaterial.matchXMaterial(product.getString("minecraftItem")).get();

        if (material == null || !material.isSupported() || material == XMaterial.AIR)
            this.material = XMaterial.matchXMaterial(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getProductDefaultMaterial()).orElse(XMaterial.DIAMOND);

        if (this.restricted) {
            this.material = XMaterial.matchXMaterial(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getRestrictedProduct().getItem()).orElse(XMaterial.BARRIER);
        }

        Optional.ofNullable(product.optString("minecraftItemModelID", null))
                .filter(id -> !id.isEmpty() && !id.equals("0"))
                .ifPresent(modelId -> this.modelId = Integer.parseInt(modelId));
    }

    /**
     * Gets item of product
     * @return product icon for gui
     */
    @SneakyThrows
    public ItemStack getProductIcon() {
        String displayName = getProductName();
        List<String> lore;
        XMaterial material = getMaterial();

        // Discount calculation and replacements
        boolean hasDiscount = getDiscount() > 0;

        // Formatters of price
        String priceFormat = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getPriceFormat()
                .replace("{price}", String.valueOf(getTotalText()))
                .replace("{rawPrice}", String.valueOf(getTotal()));
        // Formatters of discount
        String discountedPriceFormat = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getDiscountedPriceFormat()
                .replace("{price}", String.valueOf(getOriginalPriceText()))
                .replace("{rawPrice}", String.valueOf(getOriginalPrice()))
                .replace("{discountedPrice}", String.valueOf(getTotalText()))
                .replace("{rawDiscountedPrice}", String.valueOf(getTotal()));
        String discountAmountFormat = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getDiscountAmountFormat()
                .replace("{discount}", String.valueOf(getDiscountPercent()));
        String stockUnlimited = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getStockUnlimited();


        // Discount modifier of item
        if (hasDiscount) {
            displayName = displayName.replace("%discount_amount%", discountAmountFormat);
            lore = getProductLore().stream().map(key -> key.replace("%discount_amount%" , discountAmountFormat)
                    .replace("%price%", discountedPriceFormat)).collect(Collectors.toList());
        }
        else {
            displayName = displayName.replace("%discount_amount%", "");
            lore = getProductLore().stream().map(key -> key.replace("%discount_amount%" , "")
                    .replace("%price%", priceFormat)).collect(Collectors.toList());
        }

        // Stock calculation for gui item
        if (stock < 0)
            lore = lore.stream().map(key -> key.replace("%stock%" , stockUnlimited))
                    .collect(Collectors.toList());
        else
            lore = lore.stream().map(key -> key.replace("%stock%" , String.valueOf(getStock())))
                    .collect(Collectors.toList());

        // Color utils
        lore = ChatUtil.color(lore);
        displayName = ChatUtil.color(displayName);

        return ItemUtil.getItem(material, displayName, lore, getModelId());
    }
}
