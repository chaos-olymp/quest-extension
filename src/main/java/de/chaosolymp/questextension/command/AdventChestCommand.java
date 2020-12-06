package de.chaosolymp.questextension.command;

import de.chaosolymp.questextension.advent.ChestLocations;
import de.chaosolymp.questextension.advent.ConfigWriter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdventChestCommand implements CommandExecutor {
    public ChestLocations locations = ChestLocations.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player;

        if (sender instanceof Player) {
            player = (Player) sender;

            Block block = player.getTargetBlockExact(5);

            if(args[0].equals("create")){
                if(block.getType() == Material.CHEST){
                    Location loc = block.getLocation();
                    if(!locations.containsLocation(loc)){
                        ConfigWriter.addChest(loc,Integer.parseInt(args[1]));

                    }

                }
            }
            else if(args[0].equals("delete")){
                if(block.getType() == Material.CHEST){
                    Location loc = block.getLocation();
                    if(locations.containsLocation(loc)){
                        ConfigWriter.removeChest(loc,Integer.parseInt(args[1]));
                    }

                }
            }

        }
        return false;
    }
}
