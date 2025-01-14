package org.sudaping.itemevent.cr.crv;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.cr.CustomRecipe;

import static org.sudaping.itemevent.cr.CustomRecipe.getCustomRecipeByName;

public class RecipeViewCommand implements CommandExecutor {


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
