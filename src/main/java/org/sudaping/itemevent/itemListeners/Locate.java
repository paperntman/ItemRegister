package org.sudaping.itemevent.itemListeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.NBTComponent;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class Locate implements org.sudaping.itemevent.ItemEventListener {
    @Override
    public void onEvent(@NotNull PlayerInteractEvent event) {
        Location location = event.getPlayer().getLocation();
        System.out.println(event.getItem().getItemFlags());
        NBTComponent<EntityNBTComponent, EntityNBTComponent.Builder> nbtComponent = Component.entityNBT("", "topen0330");
        event.getPlayer().sendMessage(nbtComponent);
    }
}
