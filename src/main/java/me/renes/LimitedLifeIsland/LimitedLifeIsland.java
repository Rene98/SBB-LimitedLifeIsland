package me.renes.LimitedLifeIsland;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.commands.SuperiorCommand;
import com.bgsoftware.superiorskyblock.api.modules.PluginModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LimitedLifeIsland extends PluginModule {

    private static LimitedLifeIsland instance;
    private static JavaPlugin javaPlugin;

    private Settings settings;
    private MessageHandler messageHandler;


    public LimitedLifeIsland() {
        super("LimitedLifeIsland", "Rene98");

    }

    @Override
    public void onEnable(SuperiorSkyblock superiorSkyblock) {
        javaPlugin = (JavaPlugin) superiorSkyblock;
        instance = this;
        messageHandler = new MessageHandler(this);
        settings = new Settings(this);
    }
    @Override
    public void onReload(SuperiorSkyblock superiorSkyblock) {

    }

    @Override
    public void onDisable(SuperiorSkyblock superiorSkyblock) {

    }

    @Override
    public Listener[] getModuleListeners(SuperiorSkyblock superiorSkyblock) {
        return new Listener[]{new DeathListener()};
    }

    @Override
    public SuperiorCommand[] getSuperiorCommands(SuperiorSkyblock superiorSkyblock) {
        return new SuperiorCommand[0];
    }

    @Override
    public SuperiorCommand[] getSuperiorAdminCommands(SuperiorSkyblock superiorSkyblock) {
        return new SuperiorCommand[0];
    }

    public Settings getSettings() {
        return settings;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public static LimitedLifeIsland getModule() {
        return instance;
    }

    public static JavaPlugin getSBB() {
        return javaPlugin;
    }
}
