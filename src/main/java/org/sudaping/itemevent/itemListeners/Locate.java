package org.sudaping.itemevent.itemListeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.StructureSearchResult;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Main;

public class Locate implements org.sudaping.itemevent.ItemEventListener {
    @Override
    public void onEvent(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation().clone();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String structureName = persistentDataContainer.get(new NamespacedKey(Main.plugin, "structure"), PersistentDataType.STRING);
        if (structureName == null) return;
        Structure structure = Registry.STRUCTURE.get(new NamespacedKey("minecraft", structureName));
        if (structure == null) return;

        StructureSearchResult structureSearchResult = player.getWorld().locateNearestStructure(location, structure, 150000, false);
        if (structureSearchResult == null) {
            player.sendMessage(Component.translatable("commands.locate.structure.not_found",
                    Component.text("minecraft:"+structureName)));
        }else {
            Location targetLocation = structureSearchResult.getLocation();
            targetLocation.setY(0);
            location.setY(0);
            player.sendMessage(Component.translatable("commands.locate.structure.success",
                    Component.text("minecraft:"+structureName),
                    Component.text("["+(int)targetLocation.x()+", ~, "+(int)targetLocation.z()+"]", NamedTextColor.GREEN),
                    Component.text((int)targetLocation.distance(location))));
        }
        itemStack.subtract();
    }
}
