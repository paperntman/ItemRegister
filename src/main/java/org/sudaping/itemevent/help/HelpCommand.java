package org.sudaping.itemevent.help;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static org.sudaping.itemevent.ci.CustomInventoryCommand.inventoryMap;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            Inventory inventory = inventoryMap.get("help");
            if (inventory != null) {
                player.openInventory(inventory);
            }else
                sender.sendMessage(Component.text("인벤토리를 찾을 수 없습니다!", NamedTextColor.RED));
        }else
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
        return true;
    }
}
