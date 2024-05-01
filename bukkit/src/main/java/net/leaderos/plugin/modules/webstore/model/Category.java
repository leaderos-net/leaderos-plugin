package net.leaderos.plugin.modules.webstore.model;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.helpers.ChatUtil;
import net.leaderos.plugin.helpers.ItemUtil;
import net.leaderos.shared.exceptions.RequestException;
import net.leaderos.shared.model.request.GetRequest;
import net.leaderos.shared.model.request.impl.store.ListingRequest;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private boolean status;

    /**
     * Category Id
     */
    private String categoryId;

    /**
     * Parent Id
     */
    private String parentId;

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
     * Category model id
     */
    private int modelId;

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
        this.parentId = category.getString("parentID");
        // if title is empty removes it.
        try {
            this.categoryName = category.getString("minecraftTitle");
            if (this.categoryName.isEmpty())
                throw new Exception();
        }
        catch (Exception e) {
            this.categoryName = category.getString("name");
        }

        try {
            this.categoryLore = Arrays.asList(category.getString("minecraftDescription").split("\r\n"));
            if (categoryLore.isEmpty())
                throw new Exception();
        }
        catch (Exception e) {
            this.categoryLore = Bukkit.getInstance().getLangFile().getGui().getWebStoreGui().getDefaultCategory().getLore();
        }

        String materialName = category.getString("minecraftItem");
        if (materialName != null && XMaterial.matchXMaterial(materialName).isPresent())
            this.material = XMaterial.matchXMaterial(category.getString("minecraftItem")).get();

        if (material == null || !material.isSupported())
            this.material = XMaterial.matchXMaterial(Bukkit.getInstance().getModulesFile().getWebStore().getGui().getCategoryDefaultMaterial()).orElse(XMaterial.CHEST);

        Optional.ofNullable(category.optString("minecraftItemModelID", null))
                .filter(id -> !id.isEmpty() && !id.equals("0"))
                .ifPresent(modelId -> this.modelId = Integer.parseInt(modelId));

        // products
        JSONArray products = category.getJSONArray("products");
        if (!products.isEmpty())
            products.forEach(key -> {
                Product product = new Product((JSONObject) key);
                if (product.isStatus())
                    this.productList.add(product);
            });

        // subcategories
        JSONArray subcategories = category.getJSONArray("subcategories");
        if (!subcategories.isEmpty())
            subcategories.forEach(key -> {
                Category subCategory = new Category((JSONObject) key);
                if (subCategory.isStatus())
                    this.subCategories.add(subCategory);
            });
    }

    /**
     * Gets item of category
     * @return category item
     */
    public ItemStack getCategoryIcon() {
        return ItemUtil.getItem(getMaterial(), ChatUtil.color(getCategoryName()), ChatUtil.color(getCategoryLore()), getModelId());
    }

}
