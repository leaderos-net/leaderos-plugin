package net.leaderos.plugin.bukkit.modules.webstore.model;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.shared.helpers.ChatUtil;
import net.leaderos.plugin.shared.helpers.GuiHelper;
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

        // if title is empty removes it.
        try {
            this.productName = product.getString("minecraftTitle");
            if (this.productName.isEmpty())
                throw new Exception();
        }
        catch (Exception e) {
            this.productName = product.getString("name");
        }

        try {
            this.productLore = Arrays.asList(product.getString("minecraftDescription").split("\r\n"));
            if (productLore.isEmpty())
                throw new Exception();
        }
        catch (Exception e) {
            this.productLore = Main.getInstance().getLangFile().getGui().getDefaultGui().getDefaultProduct().getLore();
        }

        // price, discountedPrice, stock data
        this.price = product.getDouble("price");
        this.discountedPrice = product.getDouble("discountedPrice");
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
            this.material = XMaterial.matchXMaterial(Main.getInstance().getLangFile().getGui()
                    .getDefaultGui().getDefaultProduct().getMaterial()).get();
    }

    /**
     * Gets item of product
     * @return
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
        // Formatters of discount
        String discountedPriceFormat = Main.getInstance().getLangFile().getGui().getWebStoreGui().getDiscountedPriceFormat()
                .replace("{price}", price+"")
                .replace("{discountedPrice}", discountedPrice+"");
        String discountAmountFormat = Main.getInstance().getLangFile().getGui().getWebStoreGui().getDiscountAmountFormat()
                .replace("{discount}", discountAmount+"");
        String stockUnlimited = Main.getInstance().getLangFile().getGui().getWebStoreGui().getStockUnlimited();


        // Discount modifier of item
        if (hasDiscount) {
            displayName = displayName.replace("%discount_amount%", discountAmountFormat);
            lore = getProductLore().stream().map(key -> key.replace("%discount_amount%" , discountAmountFormat)
                    .replace("%price%", discountedPriceFormat)).collect(Collectors.toList());
        }
        else {
            displayName = displayName.replace("%discount_amount%", "");
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
