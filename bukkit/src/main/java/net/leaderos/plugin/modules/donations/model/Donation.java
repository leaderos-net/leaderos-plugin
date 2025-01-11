package net.leaderos.plugin.modules.donations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class Donation {

    /**
     * Rank for placeholder
     */
    private int rank;

    /**
     * Display Name data type of String
     */
    private String displayName;

    /**
     * Username data type of String
     */
    private String username;

    /**
     * Amount data type of double
     */
    private double amount;

    /**
     * Credits symbol for placeholder
     */
    private String symbol;
}
