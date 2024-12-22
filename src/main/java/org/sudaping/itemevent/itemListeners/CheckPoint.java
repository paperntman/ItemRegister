package org.sudaping.itemevent.itemListeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.ItemEventListener;

public class CheckPoint implements ItemEventListener {

    @Override
    public void onEvent(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        location.setY(location.getY() - 1);
        if (location.getBlock().getType().equals(Material.EMERALD_BLOCK)) {
            player.setRespawnLocation(player.getLocation());
        }
    }
}
