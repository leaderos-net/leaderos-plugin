package net.leaderos.shared.modules.credit;

import net.leaderos.shared.model.Response;
import net.leaderos.shared.model.request.GetRequest;
import net.leaderos.shared.model.request.PostRequest;
import net.leaderos.shared.model.request.impl.credit.*;
import org.jetbrains.annotations.Nullable;

/**
 * Helper Class for auth module
 * @author poyrazinan
 * @since 1.0
 */
public class CreditHelper {


    /**
     * Send credit to another player
     * @param sender executor
     * @param target player
     * @param amount of currency
     * @return response of request
     */
    public static @Nullable Response sendCreditRequest(String sender, String target, Double amount) {
        try {
            PostRequest postSendCredit = new SendCreditsRequest(sender, target, amount);
            return postSendCredit.getResponse();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets credits of player as a response
     * @param sender executor
     * @return response of request
     */
    public static @Nullable Response getRequest(String sender) {
        try {
            GetRequest getCurrency = new GetCreditsRequest(sender);
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
            PostRequest postAddCredit = new AddCreditsRequest(target, amount);
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
            PostRequest postRemoveCredit = new RemoveCreditsRequest(target, amount);
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
            PostRequest postSetCredit = new SetCreditsRequest(target, amount);
            return postSetCredit.getResponse();
        }
        catch (Exception e) {
            return null;
        }
    }
}
