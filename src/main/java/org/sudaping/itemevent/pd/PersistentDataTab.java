package org.sudaping.itemevent.pd;

import org.bukkit.Bukkit;
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
import org.sudaping.itemevent.Main;
import org.sudaping.itemevent.prefix.Prefix;
import org.sudaping.itemevent.ci.CustomInventoryCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            completions.addAll(List.of("structure", "biome", "inventory", "message", "command", "prefix", "required", "itemcommand", "fly", "death"));
       }
        if (args.length == 3 && args[0].equals("set")) {
            switch (args[1]) {
                case "structure" -> completions.addAll(Registry.STRUCTURE.stream().map(a -> a.key().value()).toList());
                case "biome" -> completions.addAll(Registry.BIOME.stream().map(a -> a.key().value()).toList());
                case "inventory" -> completions.addAll(CustomInventoryCommand.inventoryMap.keySet());
                case "message" -> {
                    if (args[2].startsWith("\\f")) {
                        File file = new File(
                                Main.plugin.getDataFolder().getAbsolutePath()
                                        + File.separator
                                        + "json");
                        File[] list = file.listFiles();
                        if (list != null) {
                            completions.addAll(Arrays.stream(list).map(File::getName).map(s -> "\\f" + s).toList());
                        }
                    }
                }
                case "prefix", "required" -> completions.addAll(Prefix.getPrefixMap().keySet());
                case "death" -> completions.add("true");
            }
        }

        if ((args.length > 3 && args[0].equals("set") && args[1].equals("command"))) {
            String trim = Arrays.stream(args).skip(3).collect(Collectors.joining(" "));
            List<String> c = Bukkit.getCommandMap().tabComplete(sender, trim);
            if (c == null) c = new ArrayList<>();
            else c = c.stream().map(s -> s.replaceFirst("/", "")).toList();
            completions.addAll(c);
        }
        if (args.length > 2 && args[0].equals("set") && args[1].equals("itemcommand")){
            String trim = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
            List<String> c = Bukkit.getCommandMap().tabComplete(sender, trim);
            if (c == null) c = new ArrayList<>();
            else c = c.stream().map(s -> s.replaceFirst("/", "")).toList();
            completions.addAll(c);
        }
        return completions.stream().filter(s -> s.startsWith(args[args.length-1])).sorted().toList();
    }
}
