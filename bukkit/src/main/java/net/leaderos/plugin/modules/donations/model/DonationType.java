package net.leaderos.plugin.modules.donations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author poyrazinan, efekurbann
 * @since 1.0
 */
@AllArgsConstructor
@Getter
public enum DonationType {

    LATEST("latest", 'l', '1'),
    TOP_ALLTIME("top-alltime", 'a', '2'),
    TOP_ANNUAL("top-annual", 'y', '3'),
    TOP_MONTHLY("top-monthly", 'm', '4'),
    TOP_DAILY("top-daily", 'd', '5');

    private final String request;
    private final char guiChar;
    private final char guiInfoChar;

}
