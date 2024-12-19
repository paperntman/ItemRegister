package org.sudaping.itemevent.eventListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.sudaping.itemevent.ItemEventListener;
import org.sudaping.itemevent.commands.Registration;
import org.sudaping.itemevent.itemListeners.Fly5m;
import org.sudaping.itemevent.itemListeners.Locate;

import java.util.HashMap;
import java.util.Map;

public class ItemEventHandler implements Listener {

    public static Map<String, ItemEventListener> map = new HashMap<>();


    public ItemEventHandler() {
        map.put("fly5m", new Fly5m());
        map.put("locate", new Locate());
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent e){
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return;

        for (Map.Entry<ItemStack, String> itemStackStringEntry : Registration.componentMap.entrySet()) {
            ItemStack t_item = itemStackStringEntry.getKey();
            String t_key = itemStackStringEntry.getValue();
            String key = null;

            if (t_item.getItemMeta().displayName().equals(item.getItemMeta().displayName()))
                key = t_key;

            if(key == null) continue;
            ItemEventListener itemEventListener = map.get(key);
            if (itemEventListener != null) {
                itemEventListener.onEvent(e);
            }
        }
    }
}
