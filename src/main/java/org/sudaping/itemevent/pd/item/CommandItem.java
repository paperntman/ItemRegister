package org.sudaping.itemevent.pd.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

import java.util.List;

public class CommandItem implements Listener {

    @EventHandler
    public void onEvent(@NotNull PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String required = persistentDataContainer.get(new NamespacedKey(Main.plugin, "required"), PersistentDataType.STRING);
        String command = persistentDataContainer.get(new NamespacedKey(Main.plugin, "itemcommand"), PersistentDataType.STRING);
        if (command == null) {
            return;
        }
        if (required == null) {
            if (player.isOp()) player.performCommand(command);
            else {
                player.setOp(true);
                player.performCommand(command);
                player.setOp(false);
            }
            return;
        }
        List<String> strings = Prefix.getPlayerPrefixMap().get(player.getUniqueId());
        if (strings == null || strings.isEmpty() || !strings.contains(required)) {
            player.sendMessage(Component.text("필요한 칭호가 없습니다!", NamedTextColor.RED));
            return;
        }
        if (player.isOp()) player.performCommand(command);
        else {
            player.setOp(true);
            player.performCommand(command);
            player.setOp(false);
        }
    }
}
