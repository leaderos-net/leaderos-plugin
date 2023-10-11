package net.leaderos.plugin.bukkit.modules.cache.model;

import lombok.Getter;
import lombok.Setter;
import net.leaderos.plugin.shared.exceptions.RequestException;
import net.leaderos.plugin.shared.model.request.PostRequest;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public User(@NotNull JSONObject user) {
        // TODO Fix
        /*
        this.id = user.getString("id");
        this.credit = Double.parseDouble(user.getString("credit"));
        this.username = user.getString("username");
        this.email = user.getString("email");
        this.creationIp = user.getString("creationIp");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.creationDate = format.parse(user.getString("creationDate"));
        }
        catch (ParseException e) {
            try {
                this.creationDate = format.parse("1000-01-01 00:00:00");
            } catch (ParseException ex) {}
        }
        */
        this.id = "1";
        this.username = "Geyik";

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
}
