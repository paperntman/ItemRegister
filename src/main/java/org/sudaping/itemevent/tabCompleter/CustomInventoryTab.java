package org.sudaping.itemevent.tabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.commands.CustomInventoryCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomInventoryTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length){
            case 1 -> completions.addAll(List.of("create", "remove", "modify", "list", "view"));
            case 2 -> {
                if (List.of("modify", "remove", "view").contains(args[0]))
                    completions.addAll(CustomInventoryCommand.inventoryMap.keySet());
            }
        }
        return completions.stream().filter(s -> s.startsWith(args[args.length-1])).sorted().collect(Collectors.toList());
    }
}
