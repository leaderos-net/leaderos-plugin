package net.leaderos.plugin.bukkit.model;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.bukkit.helpers.ChatUtil;
import net.leaderos.plugin.bukkit.helpers.GuiHelper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
     * Product price
     */
    private double price;

    /**
     * Product discount price
     */
    private double discountedPrice;

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
        this.productName = product.getString("minecraftTitle");
        // if title is empty removes it.
        if (productName.isEmpty())
            this.productName = product.getString("name");

        // checks if there is any description for it
        String description = product.getString("minecraftDescription");
        if (!description.isEmpty())
            this.productLore = Arrays.asList(description.split("\n"));
        else
            // TODO default value
            this.productLore = Arrays.asList(description.split("\n"));

        // price, discountedPrice, stock data
        this.price = Double.parseDouble(product.getString("price"));
        this.discountedPrice = Double.parseDouble(product.getString("discountedPrice"));
        this.stock = Integer.parseInt(product.getString("stock"));

        // expire date data
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.discountExpiryDate = format.parse(product.getString("creationDate"));
        }
        catch (ParseException e) {
            try {
                this.discountExpiryDate = format.parse("1000-01-01 00:00:00");
            } catch (ParseException ex) {}
        }


        String materialName = product.getString("minecraftItem");
        if (materialName != null)
            this.material = XMaterial.valueOf(product.getString("minecraftItem"));

        if (material == null || material.isSupported())
            // TODO default item
            this.material = XMaterial.DIAMOND;
    }

    /**
     * Gets item of product
     * @return
     */
    public ItemStack getProductIcon() {
        String displayName = getProductName();
        List<String> lore;

        // Discount calculation and replacements
        boolean hasDiscount = false;
        Date discountDate = getDiscountExpiryDate();

        if (getDiscountedPrice() > 0 && discountDate.after(new Date()))
            hasDiscount = true;

        int discountAmount = (int) (((getPrice() - getDiscountedPrice()) / getPrice()) * 100);
        // Formatters of discount
        String discountedPriceFormat = Main.getInstance().getLangFile().getGui().getWebStoreGui().getDiscountedPriceFormat()
                .replace("{price}", price+"")
                .replace("{discountedPrice}", discountedPrice+"");
        String discountAmountFormat = Main.getInstance().getLangFile().getGui().getWebStoreGui().getDiscountAmountFormat()
                .replace("{discount}", discountAmount+"");
        String stockUnlimited = Main.getInstance().getLangFile().getGui().getWebStoreGui().getStockUnlimited();


        // Discount modifier of item
        if (hasDiscount) {
            displayName.replace("%discount_amount%", discountAmountFormat);
            lore = getProductLore().stream().map(key -> key.replace("%discount_amount%" , discountAmountFormat)
                    .replace("%price%", discountedPriceFormat)).collect(Collectors.toList());
        }
        else {
            displayName.replace("%discount_amount%", "");
            lore = getProductLore().stream().map(key -> key.replace("%discount_amount%" , "")
                    .replace("%price%", price+"")).collect(Collectors.toList());
        }

        // Stock calculation for gui item
        if (stock < 0)
            lore = lore.stream().map(key -> key.replace("%stock%" , stockUnlimited))
                    .collect(Collectors.toList());
        else
            lore = lore.stream().map(key -> key.replace("%stock%" , getStock()+""))
                    .collect(Collectors.toList());

        // Color utils
        lore = ChatUtil.color(lore);
        displayName = ChatUtil.color(displayName);

        return GuiHelper.getItem(getMaterial(), displayName, lore);
    }
}
