package net.leaderos.plugin.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Categories of web-store
 *
 * @author poyrazinan
 * @since 1.0
 */
public class Category {

    /**
     * Category Id
     */
    @Getter
    @Setter
    private String categoryId;

    /**
     * Name of Category
     */
    @Getter
    @Setter
    private String categoryName;

    /**
     * Sub-categories of category
     */
    @Getter
    @Setter
    private List<Category> subCategories = new ArrayList<>();

    /**
     * Category constructor
     * @param category JSONObject of response
     */
    public Category(@NotNull JSONObject category) {
        this.categoryId = category.getString("id");
        this.categoryName = category.getString("name");

        category.getJSONArray("sub-categories").forEach(key -> this.subCategories.add(new Category((JSONObject) key)));
    }

}
