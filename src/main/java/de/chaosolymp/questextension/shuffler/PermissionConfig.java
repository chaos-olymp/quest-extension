package de.chaosolymp.questextension.shuffler;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PermissionConfig {

    private static File file;
    private static FileConfiguration customFile;

    public static boolean setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("QuestExtension").getDataFolder(), "questperms.yml");
        boolean exist = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                Bukkit.getLogger().info("Creating File");
            } catch (IOException e) {
                Bukkit.getLogger().info(e.toString());
            }
        } else {
            exist = true;
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        initialize();
        return exist;
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    private static void initialize(){
        customFile.addDefault("permissions.hunt.mode","random");
        customFile.addDefault("permissions.hunt.count",1);
        List<String> nodesHunt = new ArrayList<>();
        nodesHunt.add("daily.hoglinslay");
        nodesHunt.add("daily.fishkoi");
        nodesHunt.add("daily.npcdeliver");
        nodesHunt.add("daily.findbankrotis");

        customFile.addDefault("permissions.hunt.permissionlist",nodesHunt);

        customFile.addDefault("permissions.collect.mode","random");
        customFile.addDefault("permissions.collect.count",1);

        List<String> nodesCollect = new ArrayList<>();

        nodesCollect.add("daily.applecollect");
        nodesCollect.add("daily.pumpkinharvest");
        nodesCollect.add("daily.herbcollect");

        customFile.addDefault("permissions.collect.permissionlist",nodesCollect);

        customFile.addDefault("permissions.29.mode","daily");
        customFile.addDefault("permissions.29.mode",1);
        customFile.addDefault("permissions.29.permission","christmas.29");


        customFile.options().copyDefaults(true);
        save();
    }
}
