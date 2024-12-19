package org.sudaping.itemevent.listeners;

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

public class Fly5m implements ItemEventListener {

    public static final Archive archive = Archive.load(Fly.class);

    @Override
    public void onEvent(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String UUID = player.getUniqueId().toString();

        player.getInventory().getItemInMainHand().subtract();
        if (!archive.exists()) archive.write(new JsonObject().toString());
        JsonObject jsonObject = JsonParser.parseString(archive.read()).getAsJsonObject();
        jsonObject.addProperty(UUID,
                jsonObject.get(UUID) == null ? 6000 : jsonObject.get(UUID).getAsInt()
                        +6000);
        archive.write(jsonObject.toString());
        int time = jsonObject.get(UUID).getAsInt();
        player.sendMessage(Component.text("플라이 가능 시간이")
                .append(Component.text(" 5분", NamedTextColor.GREEN))
                .append(Component.text(" 추가되었습니다! ( 남은 시간 : "))
                .append(Component.text(time/72000 + "시간 "+time%72000/1200+"분 "+time%1200/20+"초 ", NamedTextColor.GREEN))
                .append(Component.text(")")));
    }
}
