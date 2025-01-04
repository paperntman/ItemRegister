package org.sudaping.itemevent.db;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugDataCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
            return true;
        }
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return true;
        }
        player.sendMessage(player.getInventory().getItemInMainHand().getItemMeta().toString());
        return true;
    }
}
