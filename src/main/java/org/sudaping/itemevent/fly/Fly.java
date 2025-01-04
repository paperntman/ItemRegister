package org.sudaping.itemevent.fly;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
            if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE){
                player.setAllowFlight(true);
                return;
            }
            JsonElement jsonElement = jsonObject.get(player.getUniqueId().toString());
            int time = jsonElement == null ? 0 : jsonElement.getAsInt();
            boolean time_able = time > ticks.getOrDefault(player, 0L);
            boolean region_able = false;
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));
            if (regionManager != null) {
                Location location = player.getLocation();
                for (ProtectedRegion region : regionManager.getRegions().values()) {
                    if (region.contains(BlockVector3.at(location.x(), location.y(), location.z()))) {
                        if (region.getFlag(WorldGuard.getInstance().getFlagRegistry().get("fly")) instanceof StateFlag.State state){
                            region_able = state.name().equals("ALLOW");
                            break;
                        }
                    }
                }
            }
            player.setAllowFlight(time_able && region_able);
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
