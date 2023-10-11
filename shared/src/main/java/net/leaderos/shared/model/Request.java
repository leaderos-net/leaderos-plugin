package net.leaderos.shared.model;

import lombok.Getter;
import lombok.SneakyThrows;
import net.leaderos.shared.Shared;
import net.leaderos.shared.exceptions.RequestException;
import net.leaderos.shared.model.request.RequestType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Request abstracted class
 *
 * @author poyrazinan
 * @since 1.0
 */
public abstract class Request {

    /**
     * body of request
     */
    private String body;

    /**
     * url of request
     */
    private URL url;

    /**
     * key of request
     */
    private String apiKey;

    /**
     * HtmlRequest object
     */
    private HttpURLConnection connection;

    /**
     * Response object
     */
    @Getter
    private Response response;

    /**
     * Request constructor
     *
     * @param api of request
     * @param body of request
     * @param type of request type (POST or GET)
     * @throws IOException for HttpUrlConnection
     * @throws RequestException for response errors
     */
    public Request(String api, Map<String, String> body, @NotNull RequestType type) throws IOException {
        this.body = encodeFormData(body);
        this.url = new URL( Shared.getInstance().getConfigFile().getSettings().getUrl()+ "/api/" + api);
        this.apiKey = Shared.getInstance().getConfigFile().getSettings().getApiKey();
        this.connection = (HttpURLConnection) this.url.openConnection();
        // Request type selector
        connection.setRequestMethod(type.name().toUpperCase());

        // Request Properties
        this.connection.setRequestProperty("X-Api-Key", this.apiKey);
        this.connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Writes body if there is any
        if (this.body != null) {
            this.connection.setDoOutput(true); // We will write OutputStream to this request
            DataOutputStream outStream = new DataOutputStream(this.connection.getOutputStream());
            outStream.writeBytes(this.body);
        }

        int responseCode = connection.getResponseCode();
        boolean status = (responseCode == HttpURLConnection.HTTP_CREATED
                || responseCode == HttpURLConnection.HTTP_ACCEPTED
                || responseCode == HttpURLConnection.HTTP_OK);
        this.response = new Response(responseCode, status, getResponseObj());
        connection.disconnect();
    }

    /**
     * Gets response of request
     *
     * @return JSONObject of response
     * @throws IOException for reader errors
     */
    private String getStream(InputStream stream) throws IOException {
        // BufferedReader for read data
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    /**
     * Gets response of request
     *
     * @return JSONObject of response
     */
    @SneakyThrows
    private JSONObject getResponseObj() {
        JSONObject response;
        String responseString = null;
        try {
            responseString = getStream(connection.getInputStream());
            response = new JSONObject(responseString);
        }
        catch (JSONException jsonException) {
            JSONArray jsonArray = new JSONArray(responseString);
            response = new JSONObject();
            response.put("array", jsonArray);
        }
        catch (IOException e) {
            response = new JSONObject(getStream(connection.getErrorStream()));
        }
        // BufferedReader for read dat
        connection.disconnect();
        // TODO REmove
        System.out.println(response);
        return response;
    }

    /**
     * Data encoder for request
     * @param data body
     * @return String value
     */
    private static String encodeFormData(Map<String, String> data) {
        try {
            StringBuilder encodedData = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (encodedData.length() != 0) {
                    encodedData.append("&");
                }
                encodedData.append(entry.getKey());
                encodedData.append("=");
                encodedData.append(entry.getValue());
            }
            return encodedData.toString();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

}
