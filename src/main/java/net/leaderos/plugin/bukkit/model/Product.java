package net.leaderos.plugin.bukkit.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Products of Categories on WebStore
 */
public class Product {

    /**
     * Product Id
     */
    @Getter
    @Setter
    private String productId;

    /**
     * Product Data Name
     */
    @Getter
    @Setter
    private String dataName;

    /**
     * Product Display Name
     */
    @Getter
    @Setter
    private String displayName;

    /**
     * Product cost
     */
    @Getter
    @Setter
    private int cost;

    /**
     * Constructor of Product
     */
    public Product(@NotNull JSONObject product) {
        this.productId = product.getString("id");
        this.dataName = product.getString("dataName");
        this.displayName = product.getString("displayName");
        this.cost = Integer.parseInt(product.getString("cost"));

    }
}
