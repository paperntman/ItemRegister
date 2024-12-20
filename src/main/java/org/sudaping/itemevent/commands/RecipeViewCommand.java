package org.sudaping.itemevent.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeViewCommand implements @Nullable CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player){
            if(args.length == 0){
                return false;
            }
            player.performCommand("cr view "+args[0]);
        }else{
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
        }
        return true;
    }
}
