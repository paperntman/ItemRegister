package org.sudaping.itemevent.eventListeners;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sudaping.itemevent.commands.Registration;

import java.util.HashMap;
import java.util.Map;

public class PlayerDeathListener implements Listener {

    private static final Map<Player, Float> expMap = new HashMap<>();
    private static final Map<Player, Integer> lvlMap = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        ItemStack target = null;
        if (e.getKeepInventory()) return;
        for (Map.Entry<ItemStack, String> itemStackStringEntry : Registration.componentMap.entrySet()) {
            ItemStack itemStack = itemStackStringEntry.getKey();
            String tag = itemStackStringEntry.getValue();

            if (tag.equalsIgnoreCase("death")) {
                target = itemStack;
            }
        }
        if (target == null) {
            return;
        }
        System.out.println(target);
        for (ItemStack drop : e.getDrops()) {
            if (drop.isSimilar(target)) {
                drop.subtract();
                e.setKeepInventory(true);
                e.getDrops().clear();
                e.setKeepLevel(true);
                e.setShouldDropExperience(false);
                return;
            }
//            ItemMeta itemMeta = drop.getItemMeta();
//            if (itemMeta == null) continue;
//            Component component = itemMeta.displayName();
//            if (component == null) continue;
//            if (component.equals(target.getItemMeta().displayName())){
//            }
        }
    }
}
