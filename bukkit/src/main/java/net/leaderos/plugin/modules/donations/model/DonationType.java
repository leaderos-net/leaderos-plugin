package net.leaderos.plugin.modules.donations.model;

/**
 * @author poyrazinan
 * @since 1.0
 */
public enum DonationType {
    LATEST,
    TOP_ALLTIME,
    TOP_ANNUAL,
    TOP_MONTHLY,
    TOP_DAILY;

    /**
     * Gets request type string
     * @return String of request
     */
    public String getRequest() {
        switch (this) {
            case LATEST:
                return "latest";
            case TOP_ALLTIME:
                return "top-alltime";
            case TOP_ANNUAL:
                return "top-annual";
            case TOP_MONTHLY:
                return "top-monthly";
            case TOP_DAILY:
                return "top-daily";
        }
        return null;
    }

    /**
     * Gets gui char
     * @return char of gui
     */
    public char getGuiChar() {
        switch (this) {
            case LATEST:
                return 'l';
            case TOP_ALLTIME:
                return 'a';
            case TOP_ANNUAL:
                return 'y';
            case TOP_MONTHLY:
                return 'm';
            case TOP_DAILY:
                return 'd';
        }
        return 'l';
    }

    /**
     * Gets gui info char
     * @return char of gui info
     */
    public char getGuiInfoChar() {
        switch (this) {
            case LATEST:
                return '1';
            case TOP_ALLTIME:
                return '2';
            case TOP_ANNUAL:
                return '3';
            case TOP_MONTHLY:
                return '4';
            case TOP_DAILY:
                return '5';
        }
        return '1';
    }
}
