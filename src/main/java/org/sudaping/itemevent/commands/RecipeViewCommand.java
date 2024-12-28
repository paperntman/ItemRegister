package org.sudaping.itemevent.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.Archive;
import org.sudaping.itemevent.CustomRecipe;

import static org.sudaping.itemevent.CustomRecipe.getCustomRecipeByName;

public class RecipeViewCommand implements @Nullable CommandExecutor {

    private static final Archive archive = Archive.load(RecipeViewCommand.class);
    private static JsonObject map;

    public RecipeViewCommand() {
        load();
    }

    public static void load() {
        String read = archive.read();
        if (read == null || read.isEmpty()) {
            map = new JsonObject();
        }else map = JsonParser.parseString(read).getAsJsonObject();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player){
            if (args.length == 0){
                return false;
            }
            String name = String.join(" ", args);
            CustomRecipe target = getCustomRecipeByName(name);
            if (target == null){
                sender.sendMessage(Component.text("존재하지 않는 레시피입니다!", NamedTextColor.RED));
                return true;
            }
            player.performCommand("cr view "+target.getKey().getKey());
        }else{
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
        }
        return true;
    }


}
