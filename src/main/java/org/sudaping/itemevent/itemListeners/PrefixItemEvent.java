package org.sudaping.itemevent.itemListeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.ItemEventListener;
import org.sudaping.itemevent.Main;
import org.sudaping.itemevent.Prefix;

public class PrefixItemEvent implements ItemEventListener {

    @Override
    public void onEvent(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String prefixName = persistentDataContainer.get(new NamespacedKey(Main.plugin, "prefix"), PersistentDataType.STRING);
        if (prefixName == null) {
            Main.logger.severe(player.getName()+"에게서 /pd 명령어로 칭호 등록이 되지 않은 칭호권을 발견했습니다!");
            player.sendMessage(Component.text("사용할 수 없는 칭호권입니다." , NamedTextColor.RED));
            return;
        }
        boolean given = Prefix.givePrefix(player.getUniqueId(), prefixName);
        if (given) {
            player.sendMessage(Component.text("칭호 ")
                    .append(Prefix.getPrefixMap().get(prefixName))
                    .append(Component.text("를 획득했습니다!")));
            player.getInventory().getItemInMainHand().subtract();
        }
    }
}
