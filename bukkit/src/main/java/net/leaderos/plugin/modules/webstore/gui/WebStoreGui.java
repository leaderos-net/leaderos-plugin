package net.leaderos.plugin.modules.webstore.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.leaderos.plugin.modules.cache.model.User;
import net.leaderos.plugin.modules.webstore.model.Category;
import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.webstore.model.Product;
import net.leaderos.shared.helpers.ChatUtil;
import net.leaderos.plugin.helpers.GuiHelper;
import net.leaderos.plugin.modules.auth.AuthLogin;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.PostRequest;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;

/**
 * Webstore gui
 * @author poyrazinan
 * @since 1.0
 */
public class WebStoreGui {

    /**
     * Constructor of gui
     */
    public WebStoreGui() {}

    /**
     * Opens gui to player
     * @param player to show gui
     * @param categoryObj category to open
     */
    public static void showGui(Player player, Category categoryObj) {
        // Gui template as array
        String[] layout = Main.getShared().getLangFile().getGui().getWebStoreGui().getLayout().toArray(new String[0]);
        // Inventory object
        String guiName = ChatUtil.color(Main.getShared().getLangFile().getGui().getWebStoreGui().getGuiName());
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, guiName, layout);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller());

        // List creator
        List<Category> categoryList;
        List<Product> productList;
        if (categoryObj == null) {
            categoryList = Category.getCategories();
            productList = new ArrayList<>();
        }
        else {
            categoryList = categoryObj.getSubCategories();
            productList = categoryObj.getProductList();
        }

        // Category group creator
        GuiElementGroup categoryGroup = new GuiElementGroup('c');
        if (!categoryList.isEmpty())
            categoryList.stream().forEach(category -> categoryGroup.addElement(new DynamicGuiElement('s', (viewer)
                    -> new StaticGuiElement('s',
                category.getCategoryIcon(),
                1,
                click ->  {
                        click.getEvent().setCancelled(true);
                        showGui(player, category);
                        return true;
                })))
            );
        gui.addElement(categoryGroup);

        // Product group creator
        GuiElementGroup productGroup = new GuiElementGroup('p');
        if (!productList.isEmpty())
            // Product Group
            productList.forEach(product -> productGroup.addElement(new DynamicGuiElement('e', (viewer)
                    -> new StaticGuiElement('e',
                    product.getProductIcon(),
                    1,
                    click ->  {
                        click.getEvent().setCancelled(true);
                        gui.close();
                        // TODO Product click event
                        if (User.isPlayerAuthed(player)) {
                            User user = User.getUser(player.getName());
                            Map<String, String> body = new HashMap<>();
                            body.put("user", user.getId());
                            String[] products = new String[1];
                            products[0] = product.getProductId();
                            body.put("products", Arrays.toString(products));

                            // Titles
                            String title = ChatUtil.color(Main.getShared().getLangFile().getGui().getWebStoreGui().getBuyWebStoreTitle());
                            String subtitleError = ChatUtil.color(Main.getShared().getLangFile().getGui().getWebStoreGui().getBuyWebStoreError());
                            String subtitleProgress = ChatUtil.color(Main.getShared().getLangFile().getGui().getWebStoreGui().getBuyWebStoreProgress());
                            String subtitleSuccess = ChatUtil.color(Main.getShared().getLangFile().getGui().getWebStoreGui().getBuyWebStoreSuccess());
                            player.sendTitle(title, subtitleProgress);
                            try {
                                // TODO FIRAT HERE
                                Response buyRequest = new PostRequest("store/buy", body).getResponse();
                                // TODO CODE
                                if (buyRequest.getResponseCode() == HttpURLConnection.HTTP_CREATED)
                                    player.sendTitle(title, subtitleSuccess);
                                else throw new IOException();
                            } catch (IOException e) {
                                // TODO Handling
                                player.sendTitle(title, subtitleError);
                                throw new RuntimeException(e);
                            }
                        }
                        else
                            AuthLogin.sendAuthModuleError(player);
                        return false;
                    })))
            );
        gui.addElement(productGroup);

        // Next and previos page icons
        gui.addElement(GuiHelper.createNextPage());
        gui.addElement(GuiHelper.createPreviousPage());
        gui.show(player);
    }
}
