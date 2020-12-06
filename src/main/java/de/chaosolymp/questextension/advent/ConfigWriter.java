package de.chaosolymp.questextension.advent;

import de.chaosolymp.questextension.QuestExtension;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class ConfigWriter {

    private static ChestLocations locations = ChestLocations.getInstance();

    public static void addChest(Location loc, int day){
        locations.addLocation(loc);
        FileConfiguration config = QuestExtension.getPlugin().getConfig();

        config.set("chests."+day+".world",loc.getWorld().getName());
        config.set("chests."+day+".x",loc.getX());
        config.set("chests."+day+".y",loc.getY());
        config.set("chests."+day+".z",loc.getZ());

        QuestExtension.getPlugin().saveConfig();
    }

    public static void removeChest(Location loc, int day){

        FileConfiguration config = QuestExtension.getPlugin().getConfig();

        config.set("chests."+day+".world",null);
        config.set("chests."+day+".x",null);
        config.set("chests."+day+".y",null);
        config.set("chests."+day+".z",null);
        config.set("chests."+day,null);

        locations.removeLocation(loc);

        QuestExtension.getPlugin().saveConfig();
    }

    public static void putPlayer(String uuid){
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        locations.getOpenedPlayers().put(uuid,today);
        FileConfiguration config = QuestExtension.getPlugin().getConfig();

        List<String> players = config.getStringList("players."+today);
        if(players == null){
            players = new ArrayList<>();
        }
        players.add(uuid);
        config.set("players."+today,players);

        QuestExtension.getPlugin().saveConfig();

    }

    public static void loadData(){
        FileConfiguration config = QuestExtension.getPlugin().getConfig();
        if(config != null){
        Set<String> set = config.getConfigurationSection("chests").getKeys(false);
        if(set != null) {
            for (String s : set) {
                ConfigurationSection section = config.getConfigurationSection("chests." + s);
                Location loc = new Location(Bukkit.getWorld(section.getString("world")), section.getInt("x"), section.getInt("y"), section.getInt("z"));
                System.out.println(loc);
                locations.addLocation(loc);
            }
        }
        }
        List<String> players = config.getStringList("players."+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        if(players != null) {
            for (String p : players) {
                locations.getOpenedPlayers().put(p, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            }
        }
    }

    public static boolean compareLoc(Location loc){
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        FileConfiguration config = QuestExtension.getPlugin().getConfig();
        String world = config.getString("chests."+today+".world");
        if(world != null) {

            if (world.equals(loc.getWorld().getName())
                    && config.getInt("chests." + today + ".x") == loc.getX()
                    && config.getInt("chests." + today + ".y") == loc.getY()
                    && config.getInt("chests." + today + ".z") == loc.getZ()) {
                return true;
            }
        }
        return false;
    }
}
