package net.leaderos.shared.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author hyperion
 * @since 1.0
 */
public class Data extends YamlConfiguration {

    /**
     * Data file
     */
    private final File file;

    /**
     * Constructor of data file
     * @param name of file
     */

    public Data(String name){
        file = new File(name);
    }

    /**
     * Crates file
     */
    public void create(){
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        reload();
    }

    /**
     * Reloads file
     */
    public void reload() {
        try {
            this.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves file
     */
    public void save() {
        try {
            this.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}