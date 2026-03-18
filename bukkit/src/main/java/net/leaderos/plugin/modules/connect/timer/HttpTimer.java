package net.leaderos.plugin.modules.connect.timer;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import net.leaderos.plugin.Bukkit;
import net.leaderos.shared.model.request.GetRequest;
import net.leaderos.shared.model.request.PostRequest;
import net.leaderos.plugin.api.managers.ModuleManager;
import net.leaderos.plugin.modules.connect.ConnectModule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP polling timer for Connect module.
 * Used when connectionMode is HTTP, or when AUTO mode falls back to HTTP.
 */
public class HttpTimer {

    /**
     * Runnable id for cancel or resume
     */
    public static WrappedTask task;

    public static boolean isRunning() {
        return task != null && !task.isCancelled();
    }

    public static void run() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        long interval = Bukkit.getInstance().getModulesFile().getConnect().getHttpTimer();
        if (interval <= 0) {
            return;
        }

        task = Bukkit.getFoliaLib().getScheduler().runTimerAsync(() -> {
            try {
                // Fetch commands in queue
                String serverToken = Bukkit.getInstance().getModulesFile().getConnect().getServerToken();
                GetRequest request = new GetRequest("command-logs/" + serverToken + "/queue");
                JSONObject response = request.getResponse().getResponseMessage();
                List<String> logIDs = new ArrayList<>();

                response.getJSONArray("array").forEach(queue -> {
                    try {
                        JSONObject queueObject = (JSONObject) queue;

                        // Only add error or stuck commands
                        if (
                                queueObject.getString("status").equals("error") || queueObject.getString("status").equals("sending")
                        ) {
                            logIDs.add(queueObject.getString("id"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                if (!logIDs.isEmpty()) {
                    // Validate commands
                    Map<String, String> formData = new HashMap<>();
                    formData.put("token", serverToken);
                    for (int i = 0; i < logIDs.size(); i++) {
                        formData.put("commands[" + i + "]", logIDs.get(i));
                    }
                    PostRequest postRequest = new PostRequest("command-logs/validate", formData);
                    JSONObject validateResponse = postRequest.getResponse().getResponseMessage();

                    if (validateResponse.has("commands")) {
                        JSONArray commandsJSON = validateResponse.getJSONArray("commands");
                        List<String> commandsList = new ArrayList<>();
                        String username = "";

                        for (Object item : commandsJSON) {
                            if (item instanceof JSONObject) {
                                JSONObject jsonItem = (JSONObject) item;
                                commandsList.add(jsonItem.getString("command"));
                                if (username.isEmpty() && jsonItem.has("username")) {
                                    username = jsonItem.getString("username");
                                }
                            }
                        }

                        // Execute validated commands
                        ConnectModule connectModule = (ConnectModule) ModuleManager.getModule("Connect");
                        connectModule.executeCommands(commandsList, username);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 1L, 20L * 60L * interval);
    }

    /**
     * Stop the HTTP polling timer
     */
    public static void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}