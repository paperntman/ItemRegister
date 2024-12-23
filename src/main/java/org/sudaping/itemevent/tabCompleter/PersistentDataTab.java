package org.sudaping.itemevent.tabCompleter;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.commands.CustomInventory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistentDataTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("set", "remove", "list").sorted().collect(Collectors.toList());
        }
        if (args.length == 2 && args[0].equals("remove")) {
            if (sender instanceof Player player){
                ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
                if (itemMeta == null) return List.of();
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                return container.getKeys().stream().map(NamespacedKey::getKey).filter(s -> s.startsWith(args[1])).sorted().collect(Collectors.toList());
            }
        }
        if (args.length == 2 && args[0].equals("set")) {
            return Stream.of("structure", "biome", "inventory", "message", "command").filter(s -> s.startsWith(args[1])).sorted().collect(Collectors.toList());
        }
        if (args.length == 3 && args[0].equals("set") && args[1].equals("structure")) {
            return Registry.STRUCTURE.stream().map(a -> a.key().value()).filter(s -> s.startsWith(args[2])).sorted().collect(Collectors.toList());
        }
        if (args.length == 3 && args[0].equals("set") && args[1].equals("biome")) {
            return Registry.BIOME.stream().map(a -> a.key().value()).filter(s -> s.startsWith(args[2])).sorted().collect(Collectors.toList());
        }
        if (args.length == 3 && args[0].equals("set") && args[1].equals("inventory")) {
            return CustomInventory.inventoryMap.keySet().stream().filter(s -> s.startsWith(args[2])).sorted().collect(Collectors.toList());
        }
        return List.of("");
    }
}
