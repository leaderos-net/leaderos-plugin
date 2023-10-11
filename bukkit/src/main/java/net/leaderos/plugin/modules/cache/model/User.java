package net.leaderos.plugin.modules.cache.model;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.leaderos.plugin.Main;
import net.leaderos.shared.model.request.GetRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * User object
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class User {

    /**
     * user cache list
     */
    private static HashMap<String, User> userList = new HashMap<>();

    /**
     * cached userList getter
     * @return cached user list
     */
    public static HashMap<String, User> getUserList() {
        return userList;
    }

    /**
     * Gets one player @Nullable
     */
    public static @Nullable User getUser(String name) {
        if (userList.containsKey(name))
            return userList.get(name);
        else return null;
    }

    /**
     * Player id on website
     */
    private String id;

    /**
     * Credit amount of player as double
     */
    @Setter
    private double credit;

    /**
     * Username of player on website
     */
    @Setter
    private String username;

    /**
     * Email of player on website
     */
    @Setter
    private String email;

    /**
     * Creation ip of player on website
     */
    private String creationIp;

    /**
     * Creation date of player on website
     */
    private Date creationDate;

    /**
     * Constructor of User object
     * @param user JSONObject of user
     */
    @SneakyThrows
    public User(@NotNull JSONObject user) {
        this.id = user.getString("id");
        this.credit = Double.parseDouble(user.getString("credit"));
        this.username = user.getString("username");
        this.email = user.getString("email");
        this.creationIp = user.getString("creationIp");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.creationDate = format.parse(user.getString("creationDate"));

        // Adds data to cache
        if (userList.containsKey(username))
            userList.remove(username);
        userList.put(username, this);
    }

    /**
     * is player authorized or not
     *
     * @param player user
     * @return boolean of status
     */
    public static boolean isPlayerAuthed(Player player) {
        if (getUser(player.getName()) == null)
            return false;
        else return true;
    }

    /**
     * loads all player data
     */
    public static void loginAllOnlinePlayers() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () ->
                Bukkit.getOnlinePlayers().forEach(player -> loginPlayer(player)));
    }

    /**
     * Login cache load method
     * @param player logined user
     */
    public static void loginPlayer(Player player) {
        try {
            GetRequest getRequest = new GetRequest("users/" + player.getName());
            new User(getRequest.getResponse().getResponseMessage());
        }
        catch (Exception e) {
            // TODO No user exception
            e.printStackTrace();
        }
    }

    /**
     * Unload player cache method
     * @param playerName name of player
     */
    public static void unloadPlayerCache(String playerName) {
        if (User.getUserList().containsKey(playerName))
            User.getUserList().remove(playerName);
    }
}
