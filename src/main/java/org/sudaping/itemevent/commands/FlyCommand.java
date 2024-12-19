package org.sudaping.itemevent.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Archive;
import org.sudaping.itemevent.runnables.Fly;

public class FlyCommand implements CommandExecutor {
    public static final Archive archive = Archive.load(Fly.class);
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player){
            JsonObject jsonObject = JsonParser.parseString(archive.read()).getAsJsonObject();
            JsonElement get = jsonObject.get(player.getUniqueId().toString());
            int timeLeft = 0;
            if(get != null){
                timeLeft = get.getAsInt();
            }
            player.sendMessage(Component.text("남은 시간 : ")
                    .append(Component.text(timeLeft/72000 + "시간 "+timeLeft%72000/1200+"분 "+timeLeft%1200/20+"초 ", NamedTextColor.GREEN)));
        }else {
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
        }
        return true;
    }
}
