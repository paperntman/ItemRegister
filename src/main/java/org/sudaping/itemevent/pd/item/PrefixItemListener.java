package org.sudaping.itemevent.pd.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Main;
import org.sudaping.itemevent.prefix.Prefix;

public class PrefixItemListener implements Listener {

    @EventHandler
    public void onEvent(@NotNull PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().equals(Material.AIR)) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String prefixName = persistentDataContainer.get(new NamespacedKey(Main.plugin, "prefix"), PersistentDataType.STRING);
        if (prefixName == null) {
            return;
        }
        boolean given = Prefix.givePrefix(player.getUniqueId(), prefixName);
        if (given) {
            player.sendMessage(Component.text("칭호 ")
                    .append(Prefix.getPrefixMap().get(prefixName))
                    .append(Component.text("를 획득했습니다!")));
            player.getInventory().getItemInMainHand().subtract();
        }
    }
}
