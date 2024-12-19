package org.sudaping.itemevent.eventListeners;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sudaping.itemevent.commands.Registration;

import java.util.Map;

public class Death implements Listener {

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
        for (ItemStack drop : e.getDrops()) {
            ItemMeta itemMeta = drop.getItemMeta();
            if (itemMeta == null) continue;
            Component component = itemMeta.displayName();
            if (component == null) continue;
            if (component.equals(target.getItemMeta().displayName())){
                drop.subtract();
                e.setKeepInventory(true);
                e.getDrops().clear();
                return;
            }
        }
    }
}
