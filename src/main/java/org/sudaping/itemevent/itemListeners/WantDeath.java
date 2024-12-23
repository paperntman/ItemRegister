package org.sudaping.itemevent.itemListeners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Archive;
import org.sudaping.itemevent.ItemEventListener;
import org.sudaping.itemevent.runnables.Fly;

public class WantDeath implements ItemEventListener {

    @Override
    public void onEvent(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        player.setHealth(0);
    }
}
