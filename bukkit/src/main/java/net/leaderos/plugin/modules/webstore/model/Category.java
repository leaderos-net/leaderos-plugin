package net.leaderos.plugin.modules.webstore.model;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.Main;
import net.leaderos.shared.exceptions.RequestException;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.shared.model.request.GetRequest;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
     * Category list
     */
    private static List<Category> categories = new ArrayList<>();

    /**
     * Getter of categories
     * @return Category List
     */
    public static List<Category> getCategories() {
        return categories;
    }

    /**
     * Status of category
     */
    private boolean status;

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
            this.categoryLore = Main.getShared().getLangFile().getGui().getDefaultGui().getDefaultCategory().getLore();
        }

        String materialName = category.getString("minecraftItem");
        if (materialName != null && XMaterial.matchXMaterial(materialName).isPresent())
            this.material = XMaterial.matchXMaterial(category.getString("minecraftItem")).get();

        if (material == null || !material.isSupported())
            this.material = XMaterial.matchXMaterial(Main.getShared().getLangFile().getGui()
                    .getDefaultGui().getDefaultCategory().getMaterial()).get();

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

        // Adds category to list
        if (category.getString("parentID").equals("0"))
            categories.add(this);
    }

    /**
     * Gets item of category
     * @return
     */
    public ItemStack getCategoryIcon() {
        return GuiHelper.getItem(getMaterial(), ChatUtil.color(getCategoryName()), ChatUtil.color(getCategoryLore()));
    }

    public static void loadAllCategories() throws IOException, RequestException {
        if (!categories.isEmpty())
            categories.clear();

        GetRequest getRequest = new GetRequest("store/listing");
        JSONObject response = getRequest.getResponse().getResponseMessage();
        response.getJSONArray("categories").forEach(jsonObj -> new Category((JSONObject) jsonObj));
    }

}
