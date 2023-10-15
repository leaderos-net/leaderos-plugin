package net.leaderos.shared.socket;

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

    // TODO THIS IS TEST METHOD FIRAT
    public static void main(String[] args) {
        try {
            // TODO THIS IS SERVER TOKEN FIRAT
            new SocketClient("2d8676260ffc79145cfb0ea736ac6a27", "abc") {
                @Override
                public void executeCommands(String command) {
                    // TODO TEST HERE FIRAT
                    System.out.println(command);
                }
            };
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private IO.Options opts = new IO.Options();
    private Socket socket;
    private String serverToken;
    private String apiToken;

    /**
     * Constructor of Client also a socket listener
     * @param serverToken room of socket
     * @throws URISyntaxException exception
     */
    public SocketClient(String apiToken, String serverToken) throws URISyntaxException {
        this.apiToken = apiToken;
        this.serverToken = serverToken;

        HashMap<String, String> auth = new HashMap<>();
        auth.put("token", apiToken);
        opts.auth = auth;

        this.socket = IO.socket("http://localhost:3000", opts);

        // Connect to socket
        socket.on(Socket.EVENT_CONNECT, args -> {
            // Join room
            socket.emit("joinRoom", serverToken);

        });

        // Listen for commands
        socket.on("commands", commands -> {
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
            socket.emit("commands", commands);
        });
        socket.connect();
    }

    /**
     * Executes commands on proxy and server modules
     * @param command command to execute
     */
    public abstract void executeCommands(String command);
}