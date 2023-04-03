package me.renes.LimitedLifeIsland;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    Settings settings = null;

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (settings == null) {
            settings = LimitedLifeIsland.getModule().getSettings();
        }
        if (e.isCancelled()) {
            return;
        }

        SuperiorPlayer suPlayer = SuperiorSkyblockAPI.getPlayer(e.getPlayer().getUniqueId());
        if (!suPlayer.hasIsland()) {
            return;
        }

        String schematicName = suPlayer.getIsland().getSchematicName();
        if (!settings.limitedIslands.containsKey(schematicName)) {
            return;
        }

        if (settings.takeLife(suPlayer) <= 0) {
            suPlayer.getIsland().disbandIsland();
        }
    }
}
