package org.sudaping.itemevent.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.Archive;

public class RecipeViewCommand implements @Nullable CommandExecutor {

    private static final Archive archive = Archive.load(RecipeViewCommand.class);
    private static JsonObject map;

    public RecipeViewCommand() {
        load();
    }

    public static void load() {
        String read = archive.read();
        if (read == null) {
            map = new JsonObject();
        }else map = JsonParser.parseString(read).getAsJsonObject();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player){
            if(args.length == 0){
                return false;
            }

        }else{
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
        }
        return true;
    }
}
