package net.leaderos.shared.modules.connect.socket;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.*;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.shared.model.request.PostRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
public abstract class SocketClient {

    private Pusher pusher;
    private String appKey = "leaderos-connect";
    private String host = "connect-socket.leaderos.net";
    private String authEndpoint = "https://connect-api.leaderos.net/broadcasting/auth";
    private String apiKey;
    private String serverToken;

    /**
     * Constructor of Client also a socket listener
     * @param apiKey api key of socket
     * @param serverToken room of socket
     */
    public SocketClient(String apiKey, String serverToken) {
        // Set variables
        this.apiKey = apiKey;
        this.serverToken = serverToken;

        // Create authorizer
        CustomHttpAuthorizer authorizer = new CustomHttpAuthorizer(authEndpoint, apiKey);

        // Set options
        PusherOptions options = new PusherOptions();
        options.setCluster("eu");
        options.setHost(host);
        options.setWsPort(6001);
        options.setWssPort(6002);
        options.setChannelAuthorizer(authorizer);
        options.setActivityTimeout(10000);
        options.setPongTimeout(5000);
        options.setMaxReconnectionAttempts(100);
        options.setMaxReconnectGapInSeconds(5);

        // Create pusher instance
        this.pusher = new Pusher(appKey, options);

        // Connect to socket
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("LeaderOS Connect: State changed from " + change.getPreviousState() + " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("LeaderOS Connect: There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        // Subscribe to server channel.
        PrivateChannel channel = pusher.subscribePrivate("private-servers." + serverToken, new PrivateChannelEventListener() {
            @Override
            public void onEvent(PusherEvent pusherEvent) {}

            @Override
            public void onAuthenticationFailure(String message, Exception e) {
                System.out.println(
                        String.format("LeaderOS Connect: Authentication failure due to [%s], exception was [%s]", message, e)
                );
            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {
                // Send connected message to console
                subscribed();
            }
        });

        // Listen for commands
        channel.bind("send-commands", new PrivateChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String s) {}

            @Override
            public void onAuthenticationFailure(String s, Exception e) {}

            @Override
            public void onEvent(PusherEvent event) {
                // Convert to JSON string
                String jsonString = event.getData();

                // Get "commands" key
                JSONObject jsonObject = new JSONObject(jsonString);

                // Convert to JSON array
                JSONArray logIDs = jsonObject.getJSONArray("commands");

                try {
                    // Validate commands
                    Map<String, String> formData = new HashMap<>();
                    formData.put("token", serverToken);
                    for (int i = 0; i < logIDs.length(); i++) {
                        formData.put("commands[" + i + "]", (String) logIDs.get(i));
                    }
                    PostRequest postRequest = new PostRequest("command-logs/validate", formData);
                    JSONObject response = postRequest.getResponse().getResponseMessage();
                    JSONArray commandsJSON = response.getJSONArray("commands");
                    String username = "";
                    List<String> commandsList = new ArrayList<>();

                    for (Object item : commandsJSON) {
                        if (item instanceof JSONObject) {
                            // Cast object to JSONObject
                            JSONObject jsonItem = (JSONObject) item;

                            // Add command to list
                            commandsList.add(jsonItem.getString("command"));

                            // Get username
                            if (username.equals(""))
                                username = jsonItem.getString("username");
                        }
                    }

                    // Execute validated commands
                    executeCommands(commandsList, username);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Listen for connection check
        channel.bind("ping", new PrivateChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String s) {}

            @Override
            public void onAuthenticationFailure(String s, Exception e) {}

            @Override
            public void onEvent(PusherEvent event) {
                // Send connected message to console
                subscribed();
            }
        });
    }

    /**
     * Executes commands on proxy and server modules
     * @param commands command to execute
     * @param username of player
     */
    public abstract void executeCommands(List<String> commands, String username);

    /**
     * Subscribed to channel
     */
    public abstract void subscribed();
}