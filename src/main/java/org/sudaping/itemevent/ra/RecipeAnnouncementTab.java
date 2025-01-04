package org.sudaping.itemevent.ra;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.prefix.Prefix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeAnnouncementTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        switch (args.length) {
            case 1 -> completions.addAll(List.of("add", "remove", "list"));
            case 2 -> {
                if (args[0].equalsIgnoreCase("add")) {
                    Iterator<Recipe> recipeIterator = sender.getServer().recipeIterator();
                    while (recipeIterator.hasNext()) {
                        Recipe recipe = recipeIterator.next();
                        if (recipe instanceof Keyed keyed) {
                            NamespacedKey key = keyed.getKey();
                            if (!RecipeAnnouncementCommand.keys.containsKey(key)) {
                                completions.add(key.toString());
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    completions.addAll(RecipeAnnouncementCommand.keys.keySet().stream().map(NamespacedKey::toString).toList());
                }
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase("add")) {
                    completions.addAll(Prefix.getPrefixMap().keySet());
                }
            }
        }

        return completions.stream().filter(s -> s.startsWith(args[args.length - 1])).sorted().collect(Collectors.toList());
    }
}
