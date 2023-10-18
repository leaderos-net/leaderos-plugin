package net.leaderos.shared.helpers;

/**
 * @author poyrazinan
 * @since 1.0
 */
public class GameUtil {


    /**
     * Checks if input instance of Integer or not.
     *
     * @param input of data
     * @return status of int or not in boolean
     */
    public static boolean isInteger(int input) {
        try {
            return input > 0;
        } catch(NumberFormatException exception) {
            return false;
        }
    }
}
