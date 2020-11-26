package de.chaosolymp.questextension;

import de.chaosolymp.questextension.stages.StageConsumed;
import fr.skytasul.quests.api.QuestsAPI;
import fr.skytasul.quests.api.stages.StageType;
import fr.skytasul.quests.gui.ItemUtils;
import fr.skytasul.quests.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestExtension extends JavaPlugin {
    private static final ItemStack consumedItem = ItemUtils.item(XMaterial.PORKCHOP, ChatColor.translateAlternateColorCodes('&',"&6Items konsumiert"));
    private static QuestExtension plugin;


    public void onEnable(){
        plugin = this;
        QuestsAPI.registerStage(new StageType<>("CONSUMED",StageConsumed.class,"Items Konsumiert", StageConsumed::deserialize,consumedItem,StageConsumed.Creator::new));
    }

    public static QuestExtension getPlugin() {
        return plugin;
    }
}
