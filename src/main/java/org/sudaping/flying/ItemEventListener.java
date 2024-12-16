package org.sudaping.flying;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface ItemEventListener {
    public void onEvent(@NotNull PlayerInteractEvent event);

}
