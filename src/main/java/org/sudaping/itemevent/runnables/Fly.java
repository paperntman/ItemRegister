package org.sudaping.itemevent.runnables;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.sudaping.itemevent.Archive;

import java.util.HashMap;
import java.util.Map;

public class Fly implements Runnable {

    public static Map<Player, Long> ticks = new HashMap<>();
    public static final Archive archive = Archive.load(Fly.class);
    private static JsonObject jsonObject;



    @Override
    public void run() {
        if (jsonObject == null) save();
        Bukkit.getServer().getOnlinePlayers().stream().filter(Player::isFlying).forEach(
                player -> {
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
                    ticks.put(player, ticks.getOrDefault(player, 0L) + 1);
                    JsonElement jsonElement = jsonObject.get(player.getUniqueId().toString());
                    int time = jsonElement == null ? 0 : jsonElement.getAsInt();
                    if (time - ticks.get(player) == 1200) player.sendMessage(Component.text("플라이 가능 시간이 1분 남았습니다.", NamedTextColor.YELLOW));
                }
        );

        Bukkit.getOnlinePlayers().forEach(player -> {
            JsonElement jsonElement = jsonObject.get(player.getUniqueId().toString());
            int time = jsonElement == null ? 0 : jsonElement.getAsInt();
            player.setAllowFlight(time > ticks.getOrDefault(player, 0L) || player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE);
        });
    }

    public static void save(){
        String read = archive.read();
        jsonObject = JsonParser.parseString(read.isEmpty() ? "{}" : read).getAsJsonObject();
        for (Map.Entry<Player, Long> playerLongEntry : ticks.entrySet()) {
            Player player = playerLongEntry.getKey();
            Long time = playerLongEntry.getValue();
            jsonObject.addProperty(player.getUniqueId().toString(),
                    jsonObject.get(player.getUniqueId().toString()).getAsInt() - time);
        }
        archive.write(jsonObject.toString());
        ticks.clear();
    }


}
