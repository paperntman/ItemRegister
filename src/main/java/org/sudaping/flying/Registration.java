package org.sudaping.flying;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class Registration implements @Nullable CommandExecutor {

    public static Map<Component, String> componentMap = new HashMap<>();

    //item-registration command
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage(Component.text("권한이 없습니다!").color(NamedTextColor.RED));
            return true;
        }
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!").color(NamedTextColor.RED));
            return true;
        }
        if (args.length == 0){
            sender.sendMessage(Component.text("태그를 지정해주세요!").color(NamedTextColor.RED));
            return true;
        }
        ItemStack targetItemStack = player.getInventory().getItemInMainHand();
        componentMap.put(targetItemStack.displayName(), args[0]);
        //TODO save
        return true;
    }
}
