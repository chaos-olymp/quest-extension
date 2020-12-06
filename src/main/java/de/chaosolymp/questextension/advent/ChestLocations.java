package de.chaosolymp.questextension.advent;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ChestLocations {

    private static ChestLocations instance;

    private ChestLocations(){ }

    public static ChestLocations getInstance(){
        if(instance == null){
            instance = new ChestLocations();
        }
        return instance;
    }

    HashSet<String> location = new HashSet<>();
    private Map<String, Integer> openedPlayers = new HashMap<>();

    public void addLocation(Location loc){
        location.add(toLocationString(loc));
    }

    public void removeLocation(Location loc){
        location.remove(toLocationString(loc));
    }

    public boolean containsLocation(Location loc){
        if(location.contains(toLocationString(loc)))
            return true;
        return false;
    }

    public Map<String, Integer> getOpenedPlayers() {
        return openedPlayers;
    }

    public void setOpenedPlayers(Map<String, Integer> openedPlayers) {
        this.openedPlayers = openedPlayers;
    }

    public static String toLocationString(Location loc){
        return loc.getWorld().getName()+loc.getX()+loc.getY()+loc.getZ();
    }
}
