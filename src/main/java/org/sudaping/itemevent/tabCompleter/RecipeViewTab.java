package org.sudaping.itemevent.tabCompleter;

import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.CustomRecipe;
import org.sudaping.itemevent.commands.CustomRecipeCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeViewTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length > 0) {
            for (CustomRecipe recipe : CustomRecipe.recipes) {
                ItemMeta itemMeta = recipe.getResult().getItemMeta();
                if (itemMeta == null) continue;
                Component component = itemMeta.displayName();
                if (component == null) continue;
                if (component instanceof TextComponent textComponent && !CustomRecipeCommand.json.get("blocked").getAsJsonArray().contains(new JsonPrimitive(textComponent.content())))
                    completions.add(textComponent.content());
            }
        }
        String join = String.join(" ", args);
        String exceptLast = String.join(" ", Arrays.copyOfRange(args, 0, args.length-1));

        return new ArrayList<>(completions.stream()
                .filter(s -> s.startsWith(join))
                .map(s -> s.replaceFirst(exceptLast, "").trim())
                .toList());
    }
}
