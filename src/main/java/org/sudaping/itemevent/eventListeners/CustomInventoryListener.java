package org.sudaping.itemevent.eventListeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.sudaping.itemevent.Main;
import org.sudaping.itemevent.commands.CustomInventory;

public class CustomInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity entity = event.getPlayer();
        if (entity instanceof Player player && CustomInventory.modifying.containsKey(player)){
            CustomInventory.inventoryMap.put(CustomInventory.modifying.get(player), event.getInventory());
            CustomInventory.save();
            CustomInventory.modifying.remove(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(Component.text("GUI", NamedTextColor.GOLD))) {
            return;
        }
        Inventory clickedInventory = event.getClickedInventory();
        if (!(clickedInventory != null && clickedInventory.getHolder() == null)) return;
        HumanEntity Clicked = event.getWhoClicked();
        if (Clicked instanceof Player player && CustomInventory.modifying.containsKey(player)) return;
        event.setCancelled(true);
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;
        if (!currentItem.hasItemMeta()) return;
        ItemMeta meta = currentItem.getItemMeta();
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        String inventory = persistentDataContainer.get(new NamespacedKey(Main.plugin, "inventory"), PersistentDataType.STRING);
        String message = persistentDataContainer.get(new NamespacedKey(Main.plugin, "message"), PersistentDataType.STRING);
        String command = persistentDataContainer.get(new NamespacedKey(Main.plugin, "command"), PersistentDataType.STRING);
        if (inventory != null){
            Inventory toOpen = CustomInventory.inventoryMap.get(inventory);
            if (toOpen != null){
                Clicked.openInventory(toOpen);
            }
        }
        if (message != null){
            Clicked.sendMessage(GsonComponentSerializer.gson().deserialize(message));
        }
        if (command != null && Clicked instanceof Player player){
            player.performCommand(command);
        }
    }
}
