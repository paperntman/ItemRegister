package org.sudaping.itemevent.tabCompleter;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.commands.CustomInventoryCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistentDataTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(List.of("set", "remove", "list"));
        }
        if (args.length == 2 && args[0].equals("remove")) {
            if (sender instanceof Player player){
                ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
                if (itemMeta == null) return List.of();
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                completions.addAll(container.getKeys().stream().map(NamespacedKey::getKey).toList());
            }
        }
        if (args.length == 2 && args[0].equals("set")) {
            completions.addAll(List.of("structure", "biome", "inventory", "message", "command"));
       }
        if (args.length == 3 && args[0].equals("set") && args[1].equals("structure")) {
            completions.addAll(Registry.STRUCTURE.stream().map(a -> a.key().value()).toList());
        }
        if (args.length == 3 && args[0].equals("set") && args[1].equals("biome")) {
            completions.addAll(Registry.BIOME.stream().map(a -> a.key().value()).toList());
        }
        if (args.length == 3 && args[0].equals("set") && args[1].equals("inventory")) {
            completions.addAll(CustomInventoryCommand.inventoryMap.keySet());
        }
        return completions.stream().filter(s -> s.startsWith(args[args.length-1])).sorted().toList();
    }
}
