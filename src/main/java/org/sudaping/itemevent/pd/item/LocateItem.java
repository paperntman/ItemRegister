package org.sudaping.itemevent.pd.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BiomeSearchResult;
import org.bukkit.util.StructureSearchResult;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Main;

public class LocateItem implements Listener {

    @EventHandler
    public void onEvent(@NotNull PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();
        Location location = player.getLocation().clone();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String structureName = persistentDataContainer.get(new NamespacedKey(Main.plugin, "structure"), PersistentDataType.STRING);
        if (structureName != null){
            structure(structureName, player, location, itemStack);
        }
        String biomeName = persistentDataContainer.get(new NamespacedKey(Main.plugin, "biome"), PersistentDataType.STRING);
        if (biomeName != null){
            biome(biomeName, player, location, itemStack);

        }
    }

    private static void biome(String biomeName, Player player, Location location, ItemStack itemStack) {
        Biome biome = Registry.BIOME.get(new NamespacedKey("minecraft", biomeName));
        if (biome == null) return;

        BiomeSearchResult BiomeSearchResult = player.getWorld().locateNearestBiome(location, 15000, biome);
        if (BiomeSearchResult == null) {
            player.sendMessage(Component.translatable("commands.locate.biome.not_found",
                    Component.text("minecraft:"+ biomeName)));
        }else {
            Location targetLocation = BiomeSearchResult.getLocation();
            targetLocation.setY(0);
            location.setY(0);
            player.sendMessage(Component.translatable("commands.locate.biome.success",
                    Component.text("minecraft:"+ biomeName),
                    Component.text("["+(int)targetLocation.x()+", ~, "+(int)targetLocation.z()+"]", NamedTextColor.GREEN),
                    Component.text((int)targetLocation.distance(location))));
        }
        itemStack.subtract();
    }

    private static void structure(String structureName, Player player, Location location, ItemStack itemStack) {
        Structure structure = Registry.STRUCTURE.get(new NamespacedKey("minecraft", structureName));
        if (structure == null) return;

        StructureSearchResult structureSearchResult = player.getWorld().locateNearestStructure(location, structure, 150000, false);
        if (structureSearchResult == null) {
            player.sendMessage(Component.translatable("commands.locate.structure.not_found",
                    Component.text("minecraft:"+ structureName)));
        }else {
            Location targetLocation = structureSearchResult.getLocation();
            targetLocation.setY(0);
            location.setY(0);
            player.sendMessage(Component.translatable("commands.locate.structure.success",
                    Component.text("minecraft:"+ structureName),
                    Component.text("["+(int)targetLocation.x()+", ~, "+(int)targetLocation.z()+"]", NamedTextColor.GREEN),
                    Component.text((int)targetLocation.distance(location))));
        }
        itemStack.subtract();
    }
}
