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
     * -- GETTER --
     *  cached userList getter
     *
     */
    @Getter
    private static HashMap<String, User> userList = new HashMap<>();

    /**
     * Gets one player @Nullable
     * @param name - player name
     * @return User data
     */
    public static @Nullable User getUser(String name) {
        return userList.getOrDefault(name, null);
    }

    /**
     * Player id on website
     */
    private final String id;

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
    private final String creationIp;

    /**
     * Creation date of player on website
     */
    private final Date creationDate;

    /**
     * Constructor of User object
     * @param user JSONObject of user
     */
    @SneakyThrows
    public User(@NotNull JSONObject user) {
        this.id = user.getString("id");
        this.credit = Double.parseDouble(user.getString("credit"));
        this.username = user.getString("realname");
        this.email = user.getString("email");
        this.creationIp = user.getString("creationIP");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.creationDate = format.parse(user.getString("creationDate"));

        // Adds data to cache
        userList.remove(username);
        userList.put(username, this);
    }

    /**
     * Adds credit of player
     * @param player target
     * @param amount credit
     */
    public static void addCreditCache(String player, double amount) {
        User user = getUser(player);
        if (user != null)
            user.setCredit(user.getCredit()+amount);
    }

    /**
     * Removes credit of player
     * @param player target
     * @param amount credit
     */
    public static void removeCreditCache(String player, double amount) {
        User user = getUser(player);
        if (user != null)
            user.setCredit(user.getCredit()-amount);
    }

    /**
     * Sets credit of player
     * @param player target
     * @param amount credit
     */
    public static void setCreditCache(String player, double amount) {
        User user = getUser(player);
        if (user != null)
            user.setCredit(amount);
    }

    /**
     * is player authorized or not
     *
     * @param player user
     * @return boolean of status
     */
    public static boolean isPlayerAuthed(Player player) {
        return getUser(player.getName()) != null;
    }

    /**
     * loads all player data
     */
    public static void loadAllPlayers() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () ->
                Bukkit.getOnlinePlayers().forEach(User::loadPlayerCache));
    }

    /**
     * Login cache load method
     * @param player logined user
     */
    public static void loadPlayerCache(Player player) {
        try {
            GetRequest getRequest = new GetRequest("users/" + player.getName());
            new User(getRequest.getResponse().getResponseMessage());
        }
        catch (Exception ignored) {}
    }

    /**
     * Unload player cache method
     * @param playerName name of player
     */
    public static void unloadPlayerCache(String playerName) {
        User.getUserList().remove(playerName);
    }
}
