package org.sudaping.itemevent.prefix;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.sudaping.itemevent.Archive;

import java.util.Arrays;
import java.util.List;

public class PrefixGetEvent implements Listener {

    private static final Archive archive = Archive.load(PrefixGetEvent.class);

    @EventHandler
    public void onPrefixGetEvent(PrefixEvent event) {
        if (archive.read().isBlank()) {
            archive.write("");
            return;
        }
        String[] input = archive.read().split("\n");
        for (String s : input) {
            String[] split = s.split(":");
            String prefix = split[0];
            String[] required = split[1].split(",");
            boolean b = Arrays.stream(required).map(String::trim).allMatch(s1 -> {
                List<String> strings = Prefix.getPlayerPrefixMap().get(event.getUuid());
                return strings.contains(s1);
            });
            if (!b) continue;
            boolean given = Prefix.givePrefix(event.getUuid(), prefix);
            if (!given) continue;
            Player player = Bukkit.getPlayer(event.getUuid());
            if (player == null) continue;
            player.sendMessage(Component.text("칭호 ")
                    .append(Prefix.getPrefixMap().get(prefix))
                    .append(Component.text("를 획득했습니다!")));
        }

    }
}
