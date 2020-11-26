package de.chaosolymp.questextension.stages;

import fr.skytasul.quests.api.stages.AbstractCountableStage;
import fr.skytasul.quests.api.stages.StageCreation;
import fr.skytasul.quests.gui.ItemUtils;
import fr.skytasul.quests.gui.creation.ItemsGUI;
import fr.skytasul.quests.gui.creation.stages.Line;
import fr.skytasul.quests.players.PlayerAccount;
import fr.skytasul.quests.players.PlayersManager;
import fr.skytasul.quests.players.PlayersManagerYAML;
import fr.skytasul.quests.structure.QuestBranch;
import fr.skytasul.quests.utils.Lang;
import fr.skytasul.quests.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class StageConsumed extends AbstractCountableStage<ItemStack> {

    private Map<ItemStack, Integer> amountsMap = new HashMap<>();

    public StageConsumed(QuestBranch branch, Map<Integer, Map.Entry<ItemStack, Integer>> objects) {
        super(branch, objects);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onConsume(PlayerItemConsumeEvent e){
        if(e.isCancelled())
            return;
        Player p = e.getPlayer();
        PlayerAccount acc = PlayersManager.getPlayerAccount(p);
        if (!branch.hasStageLaunched(acc, this))
            return;
        event(acc, p, e.getItem(),1);
    }

    @Override
    protected String getName(ItemStack itemStack) {
        return ItemUtils.getName(itemStack, true);
    }

    @Override
    protected Object serialize(ItemStack itemStack) {
        return itemStack.serialize();
    }

    @Override
    protected ItemStack deserialize(Object o) {
        return ItemStack.deserialize((Map<String, Object>) o);
    }

    public static StageConsumed deserialize(Map<String, Object> map, QuestBranch branch) {
        Map<Integer, Map.Entry<ItemStack, Integer>> objects = new HashMap<>();

        if (map.containsKey("items")) {
            List<ItemStack> list = (List<ItemStack>) map.get("items");
            for (int i = 0; i < list.size(); i++) {
                ItemStack is = list.get(i);
                is.setAmount(1);
                objects.put(i, new AbstractMap.SimpleEntry<>(is, is.getAmount()));
            }
        }

        StageConsumed stage = new StageConsumed(branch, objects);
        stage.deserialize(map);

        if (map.containsKey("remaining")) {
            PlayersManagerYAML migration = PlayersManagerYAML.getMigrationYAML();
            ((Map<String, List<ItemStack>>) map.get("remaining")).forEach((acc, items) -> {
                Map<ItemStack, Integer> itemsMap = new HashMap<>();
                for (ItemStack item : items) {
                    ItemStack itemOne = item.clone();
                    itemOne.setAmount(1);
                    itemsMap.put(itemOne, item.getAmount());
                }
                stage.migrateDatas(migration.getByIndex(acc), itemsMap);
            });
        }

        return stage;
    }

    public static class Creator extends StageCreation<StageConsumed>{

        private List<ItemStack> items;

        public Creator(Line line, boolean ending) {
            super(line, ending);
            
            ItemStack itemStack = new ItemStack(Material.PORKCHOP);
            itemStack.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&',"&6Items konsumiert"));
            line.setItem(6, itemStack, (p, item) -> {
                new ItemsGUI(items -> {
                    this.setItems(items);
                    reopenGUI(p, true);
                }, items).create(p);
            });
        }

        public void setItems(List<ItemStack> items) {
            this.items = Utils.combineItems(items);
            line.editItem(6, ItemUtils.lore(line.getItem(6), Lang.optionValue.format(this.items.size() + " Items")));
        }

        @Override
        public void start(Player p) {
            super.start(p);
            new ItemsGUI(items -> {
                setItems(items);
                reopenGUI(p, true);
            }, Collections.EMPTY_LIST).create(p);
        }

        @Override
        public void edit(StageConsumed stage) {
            super.edit(stage);
            setItems(stage.getObjects().values().stream().map(entry -> {
                ItemStack item = entry.getKey().clone();
                item.setAmount(entry.getValue());
                return item;
            }).collect(Collectors.toList()));
        }

        @Override
        public StageConsumed finishStage(QuestBranch branch) {
            Map<Integer, Map.Entry<ItemStack, Integer>> itemsMap = new HashMap<>();
            for (int i = 0; i < items.size(); i++) {
                ItemStack item = items.get(i);
                int amount = item.getAmount();
                item.setAmount(1);
                itemsMap.put(i, new AbstractMap.SimpleEntry<>(item, amount));
            }
            return new StageConsumed(branch, itemsMap);
        }

    }
}
