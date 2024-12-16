package org.sudaping.flying;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.sudaping.flying.listeners.Flying;

import java.util.HashMap;
import java.util.Map;

public class ItemEventHandler implements Listener {

    public static Map<String, ItemEventListener> map = new HashMap<>();

    public static void initialize(){
        map.put("flying", new Flying());
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent e){
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        String key = Registration.componentMap.getOrDefault(item.displayName(), null);
        if (key == null) return;
        ItemEventListener itemEventListener = map.get(key);
        if (itemEventListener != null) {
            itemEventListener.onEvent(e);
        }
    }
}
