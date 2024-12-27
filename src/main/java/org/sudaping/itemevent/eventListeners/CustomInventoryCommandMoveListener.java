package org.sudaping.itemevent.eventListeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class CustomInventoryCommandMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        Player player = event.getPlayer();
        List<BukkitTask> bukkitTasks = CustomInventoryListener.tasks.get(player);
        if (bukkitTasks == null) {
            return;
        }
        bukkitTasks.forEach(BukkitTask::cancel);
        player.sendMessage(Component.text("시전이 취소되었습니다!", NamedTextColor.RED));
        CustomInventoryListener.tasks.remove(player);
    }
}
