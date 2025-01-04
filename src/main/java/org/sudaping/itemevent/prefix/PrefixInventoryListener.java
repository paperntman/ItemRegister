package org.sudaping.itemevent.prefix;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.sudaping.itemevent.Main;

public class PrefixInventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getView().title().equals(Component.text("PREFIX", NamedTextColor.GOLD))) return;
        Inventory clickedInventory = e.getClickedInventory();
        if (!(clickedInventory != null && clickedInventory.getHolder() == null)) return;
        ItemStack currentItem = e.getCurrentItem();
        e.setCancelled(true);
        if (currentItem == null) return;
        if (currentItem.getType() == Material.NAME_TAG) {
            ItemMeta meta = currentItem.getItemMeta();
            String name = meta.getPersistentDataContainer().get(new NamespacedKey(Main.plugin, "prefix"), PersistentDataType.STRING);
            if (name == null) return;
            if (e.getWhoClicked() instanceof Player player) {
                player.performCommand("prefix set "+name);
            }
            return;
        }
        if (currentItem.getType() == Material.RED_WOOL) {
            TextComponent component = (TextComponent) e.getClickedInventory().getItem(4).getItemMeta().displayName();
            int page = Integer.parseInt(component.content().split(" ")[1]);
            e.getWhoClicked().openInventory(PrefixInventory.getInventory((Player) e.getWhoClicked(), page-1));
            return;
        }
        if (currentItem.getType() == Material.GREEN_WOOL) {
            TextComponent component = (TextComponent) e.getClickedInventory().getItem(4).getItemMeta().displayName();
            int page = Integer.parseInt(component.content().split(" ")[1]);
            e.getWhoClicked().openInventory(PrefixInventory.getInventory((Player) e.getWhoClicked(), page+1));
            return;
        }
    }
}
