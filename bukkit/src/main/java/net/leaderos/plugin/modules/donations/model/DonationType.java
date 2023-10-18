package net.leaderos.plugin.modules.donations.model;

/**
 * @author poyrazinan
 * @since 1.0
 */
public enum DonationType {
    ALLTIME,
    ANNUAL,
    DAILY,
    LATEST,
    MONTHLY;

    /**
     * Gets request type string
     * @return String of request
     */
    public String getRequest() {
        switch (this) {
            case ALLTIME:
                return "top-alltime";
            case ANNUAL:
                return "top-annual";
            case DAILY:
                return "top-daily";
            case LATEST:
                return "latest";
            case MONTHLY:
                return "top-monthly";
        }
        return null;
    }

    /**
     * Gets gui char
     */
    public char getGuiChar() {
        switch (this) {
            case ALLTIME:
                return 'a';
            case ANNUAL:
                return 'y';
            case DAILY:
                return 'd';
            case LATEST:
                return 'l';
            case MONTHLY:
                return 'm';
        }
        return 'l';
    }
}
