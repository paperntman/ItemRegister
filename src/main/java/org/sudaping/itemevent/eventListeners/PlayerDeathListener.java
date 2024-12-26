package org.sudaping.itemevent.eventListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.sudaping.itemevent.commands.ItemEventCommand;

import java.util.Map;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        ItemStack target = null;
        if (e.getKeepInventory()) return;
        for (Map.Entry<ItemStack, String> itemStackStringEntry : ItemEventCommand.componentMap.entrySet()) {
            ItemStack itemStack = itemStackStringEntry.getKey();
            String tag = itemStackStringEntry.getValue();

            if (tag.equalsIgnoreCase("death")) {
                target = itemStack;
            }
        }
        if (target == null) {
            return;
        }
        for (ItemStack drop : e.getDrops()) {
            if (target.isSimilar(drop)){
                drop.subtract();
                e.setKeepInventory(true);
                e.getDrops().clear();
                e.setKeepLevel(true);
                e.setDroppedExp(0);
                return;
            }
        }
    }
}
