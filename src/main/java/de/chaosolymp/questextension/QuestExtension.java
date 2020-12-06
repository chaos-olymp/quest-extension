package de.chaosolymp.questextension;

import de.chaosolymp.questextension.command.AdventChestCommand;
import de.chaosolymp.questextension.listener.ChestOpenListener;
import de.chaosolymp.questextension.listener.RepairCancelListener;
import de.chaosolymp.questextension.stages.StageConsumed;
import de.chaosolymp.questextension.advent.ConfigWriter;
import de.chaosolymp.questextension.shuffler.PermissionConfig;
import de.chaosolymp.questextension.shuffler.QuestPermissionShuffler;
import fr.skytasul.quests.api.QuestsAPI;
import fr.skytasul.quests.api.stages.StageType;
import fr.skytasul.quests.gui.ItemUtils;
import fr.skytasul.quests.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestExtension extends JavaPlugin {
    private static QuestExtension plugin;

    public void onEnable(){
        plugin = this;
        final ItemStack consumedItem = ItemUtils.item(XMaterial.PORKCHOP, ChatColor.translateAlternateColorCodes('&',"&6Items konsumiert"));
        QuestsAPI.registerStage(new StageType<>("CONSUMED",StageConsumed.class,"Items Konsumiert", StageConsumed::deserialize,consumedItem,StageConsumed.Creator::new));

        Bukkit.getPluginManager().registerEvents(new ChestOpenListener(), this);
        Bukkit.getPluginManager().registerEvents(new RepairCancelListener(),this);
        getCommand("adventchest").setExecutor(new AdventChestCommand());
        saveDefaultConfig();
        PermissionConfig.setup();
        ConfigWriter.loadData();

        QuestPermissionShuffler.initSwitchTask(true);
    }

    public static QuestExtension getPlugin() {
        return plugin;
    }
}
