package net.leaderos.shared.modules.connect.data;

import de.leonhard.storage.Json;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class CommandsQueue {

    private final Json json;

    @Getter
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CommandsQueue(String path) {
        json = new Json("commands_queue", path);
    }

    /**
     * Gets commands of player
     * @param username username of player
     * @return commands of player
     */
    public List<String> getCommands(String username) {
        return json.getStringList(username);
    }

    /**
     * Adds command to queue
     * @param username username of player
     * @param command command to add
     */
    public void addCommand(String username, String command) {
        executor.execute(() -> {
            List<String> list = getCommands(username);
            list.add(command);

            json.set(username, list);
        });
    }

    /**
     * Removes commands of player
     * @param username username of player
     */
    public void removeCommands(String username) {
        executor.execute(() -> json.set(username, null));
    }
}
