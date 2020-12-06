package de.chaosolymp.questextension.listener;

import de.chaosolymp.questextension.advent.ChestLocations;
import de.chaosolymp.questextension.advent.ConfigWriter;
import de.chaosolymp.questextension.util.MessageConverter;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;

public class ChestOpenListener implements Listener {

    private ChestLocations locations = ChestLocations.getInstance();
    private String invTitle = ChatColor.translateAlternateColorCodes('&',"&1Adventskalender Tag &5");

    @EventHandler ()
    public void onChestOpen(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getClickedBlock().getType() == Material.CHEST){
                if(e.getClickedBlock().getState() instanceof Chest){
                    Chest chest = (Chest) e.getClickedBlock().getState();
                    if(locations.containsLocation(chest.getLocation())) {
                        if (ConfigWriter.compareLoc(chest.getLocation())) {
                            if (locations.getOpenedPlayers().containsKey(e.getPlayer().getUniqueId().toString())) {
                                if (locations.getOpenedPlayers().get(e.getPlayer().getUniqueId().toString()) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                                    MessageConverter.sendConfMsg(e.getPlayer(),"advent-already-opened");
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                            Inventory inv = Bukkit.createInventory(null, 27, invTitle + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                            inv.setContents(chest.getInventory().getContents());
                            e.setCancelled(true);
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 0.5f);
                            e.getPlayer().openInventory(inv);
                            if(!e.getPlayer().hasPermission("qe.advent.bypass"))
                                ConfigWriter.putPlayer(e.getPlayer().getUniqueId().toString());
                        }
                        else
                            MessageConverter.sendConfMsg(e.getPlayer(),"advent-not-today");
                            e.setCancelled(true);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onChestClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().contains(invTitle)) {
            if (!e.getInventory().isEmpty()) {
                for (ItemStack item : e.getInventory().getContents()) {
                    if(item != null)
                        e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), item);
                }
            }
        }
    }
}
