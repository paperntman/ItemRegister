package org.sudaping.itemevent.prefix;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PrefixEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String prefix;
    private final UUID uuid;

    public PrefixEvent(UUID uuid, String prefix) {
        this.prefix = prefix;
        this.uuid = uuid;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public UUID getUuid() {
        return uuid;
    }
}
