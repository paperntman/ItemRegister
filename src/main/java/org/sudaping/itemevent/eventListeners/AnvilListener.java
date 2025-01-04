package org.sudaping.itemevent.eventListeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnvilListener implements Listener {
    @EventHandler
    public void onAnvil(PrepareAnvilEvent e){
        if (e.getInventory().getFirstItem() != null && e.getInventory().getSecondItem() != null
        && e.getInventory().getSecondItem().getType().equals(Material.ENCHANTED_BOOK)){
            ItemStack firstItem = e.getInventory().getFirstItem();
            ItemStack secondItem = e.getInventory().getSecondItem();
            ItemStack result = firstItem.clone();
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) secondItem.getItemMeta();

            Set<Map.Entry<Enchantment, Integer>> entries = new HashSet<>();
            entries.addAll(meta.getStoredEnchants().entrySet());
            entries.addAll(meta.getEnchants().entrySet());
            for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : entries) {
                Enchantment enchantment = enchantmentIntegerEntry.getKey();
                int level = enchantmentIntegerEntry.getValue();
                if (!enchantment.canEnchantItem(result)) continue;
                result.addUnsafeEnchantment(enchantment, level);
            }
            e.setResult(result);
        }
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent e){
        if (e.getInventory() instanceof AnvilInventory anvilInventory) {
            if (!e.getSlotType().equals(InventoryType.SlotType.RESULT)) return;
            HumanEntity humanEntity = e.getWhoClicked();
            if (!(humanEntity instanceof Player player)) return;
            int level = player.getLevel();
            int needLevel = anvilInventory.getRepairCost();
            if (needLevel > level) return;
            ItemStack itemStack = e.getCurrentItem();
            if (itemStack == null) return;
            player.setItemOnCursor(itemStack);
            player.setLevel(level - needLevel);
            anvilInventory.setFirstItem(null);
            anvilInventory.setSecondItem(null);
            anvilInventory.setResult(null);
        }
    }


}
