package net.leaderos.shared.helpers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author hyperion
 * @since 1.0
 */
public class MoneyUtil {


    /**
     * Formats money
     * @param amount of money
     * @return String of money
     */
    public static String format(double amount) {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setGroupingSize(3);

        return df.format(amount);
    }

    /**
     * parses money
     * @param amount of money
     * @return parsed double
     */
    public static Double parseDouble(double amount) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#.##", otherSymbols);
        df.setGroupingUsed(false);
        return Double.valueOf(df.format(amount));
    }
}