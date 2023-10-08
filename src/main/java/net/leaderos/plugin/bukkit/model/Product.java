package net.leaderos.plugin.bukkit.model;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
}
