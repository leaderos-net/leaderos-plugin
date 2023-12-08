package net.leaderos.shared.helpers;

/**
 * @author leaderos
 * @since 1.0.3
 */
public class UrlUtil {

    /**
     * Formats url
     * @param url of api
     * @return String of url
     */
    public static String format(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }
}
