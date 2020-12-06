package de.chaosolymp.questextension.shuffler;

import de.chaosolymp.questextension.QuestExtension;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class QuestPermissionShuffler {

    public static void addGroupPermission(String groupName, String nodeString, long time) {
        final LuckPerms api = LuckPermsProvider.get();
        api.getGroupManager().modifyGroup(groupName, group -> {
            PermissionNode.Builder builder = PermissionNode.builder(nodeString);

            builder.value(true).expiry(time, TimeUnit.MILLISECONDS);
            group.data().add(builder.build());

        });

    }

    public static void initSwitchTask(boolean isInitial){
        long timer = 0;
        if (isInitial) {
            LocalTime localTime = LocalTime.now(Clock.system(ZoneId.of("Europe/Berlin")));
            timer = 24 * 3600 - localTime.getHour() * 3600;
            timer += 60 * 60 - localTime.getMinute() * 60;
            timer += 60 - localTime.getSecond();
        } else {
            timer = 24 * 3600;
        }

        System.out.println("[QuestExtension] Scheduled next Permission update in: " + timer);
        new PermissionSwitchTask().runTaskLaterAsynchronously(QuestExtension.getPlugin(),timer*20);
    }

    static class PermissionSwitchTask extends BukkitRunnable{

        @Override
        public void run() {
            ConfigurationSection section = PermissionConfig.get().getConfigurationSection("permissions");
            if(section != null){
                Set<String> keySet = section.getKeys(false);
                Set<String> rolledPermissions = new HashSet<>();
                for(String s : keySet){
                    HashSet<String> last = new HashSet<>(section.getStringList(s+".last"));
                    if(section.getString(s+".mode").equals("random")) {
                        List<String> permissionPool = section.getStringList(s+".permissionlist");
                        List<String> newDailies = new ArrayList<>();
                        for (int i = 0; i < section.getInt(s + ".count"); i++) {
                            int maxRolls = 50;
                            while(true){
                                int index = (int) (Math.random() * permissionPool.size());
                                String rolledPermission = permissionPool.get(index);
                                if(!last.contains(rolledPermission) && !rolledPermissions.contains(rolledPermission)){
                                    rolledPermissions.add(rolledPermission);
                                    newDailies.add(rolledPermission);
                                    break;
                                }
                                maxRolls--;
                                if(maxRolls <= 0) break;
                            }
                        }
                        PermissionConfig.get().set("permissions."+s+".last",newDailies);
                        PermissionConfig.save();
                    }
                    else{
                        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        String permission = section.getString(""+today+".permission");
                        if(permission != null) rolledPermissions.add(permission);
                    }
                }
                for(String node : rolledPermissions){
                    addGroupPermission("default",node,(1000L*60*60*24)-1000);
                    System.out.println("added pemission "+node);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            initSwitchTask(false);
        }
    }
}
