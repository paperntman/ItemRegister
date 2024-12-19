package org.sudaping.itemevent;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface ItemEventListener {
    void onEvent(@NotNull PlayerInteractEvent event);

}
