package org.sudaping.itemevent.tabCompleter;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.commands.RecipeAnnouncement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeAnnouncementTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1: {
                return Stream.of("add", "remove", "list").filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            }
            case 2: {
                if(args[0].equalsIgnoreCase("add")){
                    Iterator<Recipe> recipeIterator = sender.getServer().recipeIterator();
                    List<String> ret = new ArrayList<>();
                    while (recipeIterator.hasNext()) {
                        Recipe recipe = recipeIterator.next();
                        if (recipe instanceof Keyed keyed) {
                            NamespacedKey key = keyed.getKey();
                            if ((key.toString().startsWith(args[1]))&& !RecipeAnnouncement.keys.contains(key)) ret.add(key.toString());
                        }
                    }
                    return ret;
                }else if(args[0].equalsIgnoreCase("remove")){
                    return RecipeAnnouncement.keys.stream().map(NamespacedKey::toString).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                }
            }
        }
        return List.of();
    }
}
