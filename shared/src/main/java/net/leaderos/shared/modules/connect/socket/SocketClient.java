package net.leaderos.shared.modules.connect.socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import net.leaderos.shared.model.request.PostRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
public abstract class SocketClient {

    private IO.Options opts = new IO.Options();
    private Socket socket;
    private String url = "https://connect.leaderos.net";
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
            JSONArray logIDs = jsonArray.getJSONArray(0);

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

                executeCommands(commandsList, username);

                // Sends commands back if everything is ok
                socket.emit("sent", commandsJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        socket.on("ping", args -> {
            socket.emit("pong");
            joinedRoom();
        });

        socket.connect();
    }

    /**
     * Executes commands on proxy and server modules
     * @param commands command to execute
     * @param username of player
     */
    public abstract void executeCommands(List<String> commands, String username);

    /**
     * Joined room abstracted method for debug
     */
    public abstract void joinedRoom();
}