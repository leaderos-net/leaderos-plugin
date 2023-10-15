package net.leaderos.shared.socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author poyrazinan
 * @since 1.0
 */
@Getter
@Setter
public abstract class Client {

    // TODO THIS IS TEST METHOD FIRAT
    public static void main(String[] args) {
        try {
            // TODO THIS IS SERVER TOKEN FIRAT
            new Client("abc", "abc") {
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
    public Client(String apiToken, String serverToken) throws URISyntaxException {
        this.apiToken = apiToken;
        this.serverToken = serverToken;
        HashMap<String, String> auth = new HashMap<>();
        // TODO Token
        auth.put("token", apiToken);
        opts.auth = auth;
        this.socket = IO.socket("http://localhost:3000", opts);
        socket.on(Socket.EVENT_CONNECT, args -> {
            // Joins room
            socket.emit("joinRoom", serverToken);

        }).on("commands", args -> {
            System.out.println(Arrays.toString(args));
            //System.out.println(new JSONObject(new JSONArray(Arrays.toString(args)).get(0)).toString());
            //JSONArray commands = new JSONObject(new JSONArray(Arrays.toString(args)).get(0)).getJSONArray("commands");
            //sendCommands(commands);
            // TODO HERE
            Arrays.asList(args).forEach(key -> {
                executeCommands((String) key);
            });
            // Sends commands back if everything is ok
            socket.emit("commands", args);
        });
        socket.connect();
    }

    /**
     * Executes commands on proxy and server modules
     * @param command command to execute
     */
    public abstract void executeCommands(String command);
}