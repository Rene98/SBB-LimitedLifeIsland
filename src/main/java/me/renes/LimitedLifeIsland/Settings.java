package me.renes.LimitedLifeIsland;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Settings {

    private MessageHandler messageHandler = null;
    public HashMap<String, Integer> limitedIslands = new HashMap<>();
    public Settings(LimitedLifeIsland limitedLifeIsland) {
        if (messageHandler == null) {
            messageHandler = limitedLifeIsland.getMessageHandler();
        }
        File file = new File(limitedLifeIsland.getDataFolder(), "config.yml");

        if (!file.exists()) {
            limitedLifeIsland.saveResource("config.yml");
        }
        YamlConfiguration configYAML = YamlConfiguration.loadConfiguration(file);
        messageHandler.prefixMsg = configYAML.getString("pluginPrefix");
        messageHandler.deathMsg = configYAML.getString("messages.deathMessage");
        messageHandler.disbandMsg = configYAML.getString("messages.disbandMessage");


        File storeDir = new File(limitedLifeIsland.getDataFolder() + "/islandData");
        if (!storeDir.exists()){
            storeDir.mkdir();
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection csLifes = cfg.getConfigurationSection("islandLifes");
        for (String key : csLifes.getKeys(false)) {
            limitedIslands.put(key,csLifes.getInt(key));
        }
    }

    public int getLifes(Player p) {
        SuperiorPlayer suPlayer = SuperiorSkyblockAPI.getPlayer(p.getUniqueId());
        if (!limitedIslands.containsKey(suPlayer.getIsland().getSchematicName())) {
            return Integer.MAX_VALUE;
        }
        String path = "/islandData/" + suPlayer.getIsland().getUniqueId() + ".yml";
        File islandLifes = new File(LimitedLifeIsland.getModule().getDataFolder() + path);
        if (!islandLifes.exists()) {
            return limitedIslands.get(suPlayer.getIsland().getSchematicName());
        }
        YamlConfiguration islandYaml = YamlConfiguration.loadConfiguration(islandLifes);
        return islandYaml.getInt("remainingLifes");
    }

    public int takeLife(SuperiorPlayer suPlayer) {
        if (!limitedIslands.containsKey(suPlayer.getIsland().getSchematicName())) {
            return Integer.MAX_VALUE;
        }
        int lifesLeft = limitedIslands.get(suPlayer.getIsland().getSchematicName()) - 1;
        if (lifesLeft <= 0) {
            messageHandler.colorSend(suPlayer.asPlayer(), true, messageHandler.disbandMsg);
            return 0;
        }
        String path = "/islandData/" + suPlayer.getIsland().getUniqueId() + ".yml";
        File islandLifes = new File(LimitedLifeIsland.getModule().getDataFolder() + path);
        if (!islandLifes.exists()) {
            try {
                islandLifes.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            YamlConfiguration islandYaml = YamlConfiguration.loadConfiguration(islandLifes);
            islandYaml.set("remainingLifes", lifesLeft);
            try {
                islandYaml.save(islandLifes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            messageHandler.colorSend(suPlayer.asPlayer(), true, messageHandler.deathMsg);
            return lifesLeft;
        } else {
            YamlConfiguration islandYaml = YamlConfiguration.loadConfiguration(islandLifes);
            lifesLeft = islandYaml.getInt("remainingLifes") - 1;
            if (lifesLeft <= 0) {
                islandLifes.delete();
                return 0;
            } else {
                islandYaml.set("remainingLifes", lifesLeft);
                try {
                    islandYaml.save(islandLifes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                messageHandler.colorSend(suPlayer.asPlayer(), true, messageHandler.deathMsg);
                return lifesLeft;
            }
        }
    }
}
