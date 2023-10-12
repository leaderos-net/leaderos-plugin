package net.leaderos.plugin.modules.credit;

import net.leaderos.plugin.Main;
import net.leaderos.plugin.modules.credit.commands.Commands;
import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.GetRequest;
import net.leaderos.shared.model.request.PostRequest;
import net.leaderos.shared.module.LeaderOSModule;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Credit module of leaderos-plugin
 *
 * @author poyrazinan
 * @since 1.0
 */
public class Credit extends LeaderOSModule {

    /**
     * onEnable method of module
     */
    public void onEnable() {
        Main.getCommandManager().registerCommand(new Commands());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        Main.getCommandManager().unregisterCommand(new Commands());
    }

    /**
     * Send credit to another player
     * @param sender executor
     * @param target player
     * @param amount of currency
     * @return response of request
     */
    public static @Nullable Response sendCreditRequest(String sender, String target, Double amount) {
        try {
            Map<String, String> list = new HashMap<>();
            list.put("target", target);
            list.put("amount", amount+"");
            PostRequest postSendCredit = new PostRequest("credits/" + sender + "/send", list);
            return postSendCredit.getResponse();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets currency of player as a response
     * @param sender executor
     * @return response of request
     */
    public static @Nullable Response currencyRequest(String sender) {
        try {
            GetRequest getCurrency = new GetRequest("credits/" + sender);
            return getCurrency.getResponse();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Adds credit to target player
     * @param target to deposit
     * @param amount amount of currency
     * @return Response of request
     */
    public static @Nullable Response addCreditRequest(String target, double amount) {
        try {
            Map<String, String> list = new HashMap<>();
            list.put("amount", amount+"");
            PostRequest postAddCredit = new PostRequest("credits/" + target + "/add", list);
            return postAddCredit.getResponse();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Removes credit to target player
     * @param target to deposit
     * @param amount amount of currency
     * @return Response of request
     */
    public static @Nullable Response removeCreditRequest(String target, double amount) {
        try {
            Map<String, String> list = new HashMap<>();
            list.put("amount", amount+"");
            PostRequest postRemoveCredit = new PostRequest("credits/" + target + "/remove", list);
            return postRemoveCredit.getResponse();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets credit to target player
     * @param target to deposit
     * @param amount amount of deposit
     * @return Response of request
     */
    public static @Nullable Response setCreditRequest(String target, double amount) {
        try {
            Map<String, String> list = new HashMap<>();
            list.put("amount", amount+"");
            PostRequest postSetCredit = new PostRequest("credits/" + target + "/set", list);
            return postSetCredit.getResponse();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Constructor of Credit
     */
    public Credit() {}
}