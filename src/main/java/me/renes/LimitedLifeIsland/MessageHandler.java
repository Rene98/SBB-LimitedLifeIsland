package me.renes.LimitedLifeIsland;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageHandler {

    private final LimitedLifeIsland LLI;
    private Settings settings;

    public MessageHandler(LimitedLifeIsland LLI) {
        this.LLI = LLI;
    }
    public String prefixMsg = "";
    public String deathMsg = "";
    public String disbandMsg = "";

    public void colorSend(Player p, boolean prefix, String msg) {
        for (SuperiorPlayer islandMember : SuperiorSkyblockAPI.getPlayer(p).getIsland().getIslandMembers(true)) {
            if (islandMember.isOnline()) {
                islandMember.asPlayer().sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                        ((prefix ? prefixMsg : "") + parsePlaceholders(msg, p))));
            }
        }
    }

    private String parsePlaceholders(String msg, Player p) {
        if (settings == null) {
            settings = LLI.getSettings();
        }
        msg = msg.replace("%SBB-LLI-remainingLifes%", Integer.toString(settings.getLifes(p)));
        msg = msg.replace("%playerName%", p.getName());
        return msg;
    }
}
