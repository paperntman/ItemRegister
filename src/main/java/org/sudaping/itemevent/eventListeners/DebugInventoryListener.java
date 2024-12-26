package org.sudaping.itemevent.eventListeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.sudaping.itemevent.CustomRecipe;

import java.util.Arrays;

public class DebugInventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().title().equals(Component.text("DEBUG", NamedTextColor.GOLD)) && e.getClickedInventory() != null && e.getClickedInventory().getHolder() == null) {
            ItemStack currentItem = e.getCurrentItem();
            if (currentItem == null) {
                e.setCancelled(true);
                return;
            }
            if (e.isLeftClick()){
                CustomRecipe[] customRecipe = CustomRecipe.getCustomRecipe(currentItem);
                handle(e, customRecipe);
            }
            if (e.isRightClick()){
                CustomRecipe[] customRecipe = CustomRecipe.recipes.stream().filter(c -> c.getIngredients().contains(currentItem)).toArray(CustomRecipe[]::new);
                handle(e, customRecipe);
            }
            if (e.isShiftClick() && e.getWhoClicked().isOp()){
                e.getWhoClicked().getInventory().addItem(currentItem);
                return;
            }
            e.setCancelled(true);
        }
    }

    private static void handle(InventoryClickEvent e, CustomRecipe[] customRecipe) {
        if (customRecipe == null || customRecipe.length == 0) {
            return;
        }
        if (customRecipe.length == 1){
            if (e.getWhoClicked() instanceof Player player) player.performCommand("cr view "+ customRecipe[0].getKey().getKey());
        }
        else {
            Inventory inventory = Bukkit.createInventory(null, 9, Component.text("DEBUG", NamedTextColor.GOLD));
            inventory.setContents(Arrays.stream(customRecipe).map(CustomRecipe::getResult).toArray(ItemStack[]::new));
            e.getWhoClicked().openInventory(inventory);
        }
    }
}
