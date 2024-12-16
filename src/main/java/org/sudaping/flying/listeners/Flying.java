package org.sudaping.flying.listeners;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.sudaping.flying.ItemEventListener;

public class Flying implements ItemEventListener {

    //TODO Logger처럼 클래스 받아서 클래스마다 자동으로 값 파일에 저장해주는 저장소 있으면 좋겠다.

    @Override
    public void onEvent(@NotNull PlayerInteractEvent event) {
        event.getPlayer().getInventory().getItemInMainHand().subtract();

    }
}
