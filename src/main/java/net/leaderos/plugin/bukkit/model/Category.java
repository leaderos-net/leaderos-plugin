package net.leaderos.plugin.bukkit.model;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Categories of web-store
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
public class Category {

    /**
     * Status of category
     */
    private boolean status = true;

    /**
     * Category Id
     */
    private String categoryId;

    /**
     * Name of Category
     */
    private String categoryName;

    /**
     * Lore of category
     */
    private List<String> categoryLore;

    /**
     * Material of category item
     */
    private XMaterial material;

    /**
     * Sub-categories of category
     */
    private List<Category> subCategories = new ArrayList<>();

    /**
     * Products of category
     */
    private List<Product> productList = new ArrayList<>();

    /**
     * Category constructor
     * @param category JSONObject of response
     */
    public Category(@NotNull JSONObject category) {
        // Checks if category active or not
        if (category.getString("isActive").equals("1")
                && category.getString("minecraftStatus").equals("1"))
            this.status = true;
        else {
            this.status = false;
            return;
        }
        this.categoryId = category.getString("id");
        this.categoryName = category.getString("minecraftTitle");
        // if title is empty removes it.
        if (categoryName.isEmpty())
            this.categoryName = category.getString("name");

        // checks if there is any description for it
        String description = category.getString("minecraftDescription");
        if (!description.isEmpty())
            this.categoryLore = Arrays.asList(description.split("\n"));
        else
            // TODO default value
            this.categoryLore = Arrays.asList(description.split("\n"));


        String materialName = category.getString("minecraftItem");
        if (materialName != null)
            this.material = XMaterial.valueOf(category.getString("minecraftItem"));

        if (material == null || material.isSupported())
            // TODO default item
            this.material = XMaterial.DIAMOND;

        // products
        JSONArray products = category.getJSONArray("products");
        if (!products.isEmpty())
            products.forEach(key -> this.productList.add(new Product((JSONObject) key)));

        // subcategories
        JSONArray subcategories = category.getJSONArray("subcategories");
        if (!subcategories.isEmpty())
            subcategories.forEach(key -> this.subCategories.add(new Category((JSONObject) key)));
    }

}
