package org.sudaping.itemevent.tabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.Main;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeAdderTab implements TabCompleter{
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1:{
                return Stream.of("add", "remove", "view").filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            }
            case 2:{
                if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("view")){
                    File file = new File(Main.dataFolder + File.separator + "recipes");
                    if (!file.exists()) return List.of();
                    String[] list = file.list();
                    if (list == null) return List.of();
                    return Arrays.stream(list).filter(e -> e.startsWith(args[1])).toList();
                }
            }
        }
        return List.of();
    }
}
