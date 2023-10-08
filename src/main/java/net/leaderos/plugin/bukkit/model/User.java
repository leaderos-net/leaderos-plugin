package net.leaderos.plugin.bukkit.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User object
 *
 * @author poyrazinan
 * @since 1.0
 */
@Getter
public class User {

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
    }
}
