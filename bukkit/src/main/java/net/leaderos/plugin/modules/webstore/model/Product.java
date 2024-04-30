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
     * dateFormatter
     */
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
     * Product price
     */
    private double price;

    /**
     * Product price text with currency
     */
    private String priceText;

    /**
     * Product discount price
     */
    private double discountedPrice;

    /**
     * Product discounted price text with currency
     */
    private String discountedPriceText;

    /**
     * Discount expiry date
     */
    private Date discountExpiryDate;

    /**
     * Product stock
     */
    private int stock;

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
        this.price = product.getDouble("price");
        this.priceText = product.getString("priceText");
        this.discountedPrice = product.getDouble("discountedPrice");
        this.discountedPriceText = product.getString("discountedPriceText");
        this.stock = product.getInt("stock");

        // expire date data
        try {
            this.discountExpiryDate = format.parse(product.getString("discountExpiryDate"));
        }
        catch (ParseException e) {
            try {
                this.discountExpiryDate = format.parse("1000-01-01 00:00:00");
            } catch (ParseException ex) {}
        }


        String materialName = product.getString("minecraftItem");
        if (materialName != null && XMaterial.matchXMaterial(materialName).isPresent())
            this.material = XMaterial.matchXMaterial(product.getString("minecraftItem")).get();

        if (material == null || !material.isSupported())
            this.material = XMaterial.matchXMaterial(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getProductDefaultMaterial()).orElse(XMaterial.DIAMOND);

        String modelId = product.getString("minecraftItemModelID");
        if (modelId != null && !modelId.isEmpty())
            this.modelId = Integer.parseInt(modelId);
    }

    /**
     * Gets item of product
     * @return product icon for gui
     */
    @SneakyThrows
    public ItemStack getProductIcon() {
        String displayName = getProductName();
        List<String> lore;

        // Discount calculation and replacements
        boolean hasDiscount = false;
        Date discountDate = getDiscountExpiryDate();

        if (getDiscountedPrice() > 0
                && (discountDate.after(new Date()) || format.format(discountDate).equals("1000-01-01 00:00:00")))
            hasDiscount = true;

        int discountAmount = (int) (((getPrice() - getDiscountedPrice()) / getPrice()) * 100);
        // Formatters of price
        String priceFormat = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getPriceFormat()
                .replace("{price}", String.valueOf(priceText))
                .replace("{rawPrice}", String.valueOf(price));
        // Formatters of discount
        String discountedPriceFormat = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getDiscountedPriceFormat()
                .replace("{price}", String.valueOf(priceText))
                .replace("{rawPrice}", String.valueOf(price))
                .replace("{discountedPrice}", String.valueOf(discountedPriceText))
                .replace("{rawDiscountedPrice}", String.valueOf(discountedPrice));
        String discountAmountFormat = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getDiscountAmountFormat()
                .replace("{discount}", String.valueOf(discountAmount));
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

        return ItemUtil.getItem(getMaterial(), displayName, lore, getModelId());
    }
}
