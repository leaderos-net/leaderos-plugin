package net.leaderos.shared.modules.connect.socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
public abstract class SocketClient {

    private IO.Options opts = new IO.Options();
    private Socket socket;
    private String url = "http://localhost:3000";
    private String apiKey;
    private String serverToken;

    /**
     * Constructor of Client also a socket listener
     * @param apiKey api key of socket
     * @param serverToken room of socket
     * @throws URISyntaxException exception
     */
    public SocketClient(String apiKey, String serverToken) throws URISyntaxException {
        this.apiKey = apiKey;
        this.serverToken = serverToken;

        HashMap<String, String> auth = new HashMap<>();
        auth.put("apiKey", apiKey);
        auth.put("token", serverToken);
        opts.auth = auth;

        this.socket = IO.socket(url, opts);

        // Error listener
        socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            System.out.println("Socket Error: " + Arrays.toString(args));
        });

        // Connect to socket
        socket.on(Socket.EVENT_CONNECT, args -> {
            // Join room
            socket.emit("joinRoom", serverToken);
            joinedRoom();
        });

        // Listen for commands
        socket.on("sendCommands", commands -> {
            // Convert to JSON string
            String jsonString = Arrays.toString(commands);

            // Convert to JSON array
            JSONArray jsonArray = new JSONArray(jsonString);

            // Get the first inner array
            JSONArray innerArray = jsonArray.getJSONArray(0);

            // Loop through the inner array
            for (Object item : innerArray) {
                if (item instanceof JSONObject) {
                    // Cast object to JSONObject
                    JSONObject jsonItem = (JSONObject) item;

                    // Get the command
                    String command = jsonItem.getString("command");

                    // Execute the command
                    executeCommands(command);
                }
            }

            // Sends commands back if everything is ok
            socket.emit("sent", commands);
        });
        socket.connect();
    }

    /**
     * Executes commands on proxy and server modules
     * @param command command to execute
     */
    public abstract void executeCommands(String command);

    /**
     * Joined room abstracted method for debug
     */
    public abstract void joinedRoom();
}