package org.sudaping.itemevent.pd.item;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Archive;
import org.sudaping.itemevent.Main;
import org.sudaping.itemevent.fly.Fly;

public class FlyItem implements Listener {

    public static final Archive archive = Archive.load(Fly.class);

    @EventHandler
    public void onEvent(@NotNull PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getHand() == EquipmentSlot.OFF_HAND) return;
        ItemMeta itemMeta = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String  timeToAdd = persistentDataContainer.get(new NamespacedKey(Main.plugin, "fly"), PersistentDataType.STRING);
        if (timeToAdd == null) {
            return;
        }
        int parse = Integer.parseInt(timeToAdd);
        Player player = event.getPlayer();
        String UUID = player.getUniqueId().toString();

        player.getInventory().getItemInMainHand().subtract();
        if (!archive.exists()) archive.write(new JsonObject().toString());
        JsonObject jsonObject = JsonParser.parseString(archive.read()).getAsJsonObject();
        jsonObject.addProperty(UUID,
                jsonObject.get(UUID) == null ? parse*1200 : jsonObject.get(UUID).getAsInt()
                        + parse*1200);
        archive.write(jsonObject.toString());
        int time = jsonObject.get(UUID).getAsInt();
        player.sendMessage(Component.text("플라이 가능 시간이")
                .append(Component.text(" "+ parse +"분", NamedTextColor.GREEN))
                .append(Component.text(" 추가되었습니다! ( 남은 시간 : "))
                .append(Component.text(time/72000 + "시간 "+time%72000/1200+"분 "+time%1200/20+"초 ", NamedTextColor.GREEN))
                .append(Component.text(")")));
    }
}
