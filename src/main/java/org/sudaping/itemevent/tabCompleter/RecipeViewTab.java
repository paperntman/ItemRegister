package org.sudaping.itemevent.tabCompleter;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.CustomRecipe;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeViewTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return CustomRecipe.recipes.stream().map(CustomRecipe::getKey).map(NamespacedKey::getKey).collect(Collectors.toList());
        }
        return List.of("");
    }
}
