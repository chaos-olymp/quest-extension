package de.chaosolymp.questextension.listener;

import de.chaosolymp.questextension.util.MessageConverter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class RepairCancelListener implements Listener {

    @EventHandler
    public void onGrindStone(InventoryClickEvent e){
        if(e.getClickedInventory().getType() == InventoryType.GRINDSTONE){
            if(!e.getWhoClicked().hasPermission("qe.repair.bypass")){
                if(e.getSlotType() == InventoryType.SlotType.RESULT) {
                    if (e.getCurrentItem().getType() == Material.TRIDENT) {
                        MessageConverter.sendConfMsg((Player) e.getWhoClicked(),"no-repair");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
