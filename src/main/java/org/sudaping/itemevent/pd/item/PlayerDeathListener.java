package org.sudaping.itemevent.pd.item;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.sudaping.itemevent.Main;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getKeepInventory()) return;
        for (ItemStack drop : e.getDrops()) {
            ItemMeta itemMeta = drop.getItemMeta();
            if (itemMeta == null) continue;
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            String death = persistentDataContainer.get(new NamespacedKey(Main.plugin, "death"), PersistentDataType.STRING);
            if (death == null) continue;
            if (death.equals("true")){
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
