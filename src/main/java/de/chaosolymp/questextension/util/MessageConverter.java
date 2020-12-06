package de.chaosolymp.questextension.util;

import de.chaosolymp.questextension.QuestExtension;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageConverter {

    public static void sendMsg(Player player, String message){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
    }

    public static void sendConfMsg(Player player, String path) {
        sendMsg(player, QuestExtension.getPlugin().getConfig().getString(path));
    }
}
