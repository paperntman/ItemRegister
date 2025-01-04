package org.sudaping.itemevent.pd;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.Main;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PersistentDataCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return true;
        }
        if (!(sender instanceof Player player)){
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
            return true;
        }
        if (args.length == 0 || !List.of("set", "remove", "list").contains(args[0])){
            sender.sendMessage(Component.text("명령어를 입력해 주세요!", NamedTextColor.RED));
            return true;
        }
        if (args.length == 1 && !args[0].equals("list")){
            sender.sendMessage(Component.text("태그를 입력해 주세요!", NamedTextColor.RED));
            return true;
        }
        if (args.length == 2 && args[0].equals("set")){
            sender.sendMessage(Component.text("상태를 입력해 주세요!", NamedTextColor.RED));
            return true;
        }
        ItemMeta itemMeta = getItemMeta(sender, player);
        if (itemMeta == null) return true;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (args[0].equalsIgnoreCase("set")){
            String collect = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
            container.set(new NamespacedKey(Main.plugin, args[1]), PersistentDataType.STRING, collect);
            player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")){
            container.remove(new NamespacedKey(Main.plugin, args[1]));
            player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
            return true;
        }
        if (args[0].equalsIgnoreCase("list")){
            for (NamespacedKey key : container.getKeys()) {
                sender.sendMessage(key.asString() + "=" +container.get(key, PersistentDataType.STRING));
            }
        }
        return true;
    }

    private static @Nullable ItemMeta getItemMeta(@NotNull CommandSender sender, Player player) {
        ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
        if (itemMeta == null) {
            sender.sendMessage("지속 데이터를 넣을 수 없는 형식의 아이템입니다!");
            return null;
        }
        return itemMeta;
    }
}
