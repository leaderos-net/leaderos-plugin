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
public class DonatorData {

    /**
     * Username data type of String
     */
    private String userName;

    /**
     * Credit data type of double
     */
    private double credit;

    /**
     * Credits symbol for placeholder
     */
    private String symbol;
}
