package org.sudaping.itemevent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.sudaping.itemevent.commands.PrefixCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PrefixInventory {
    public static Inventory getInventory(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(null, 54, Component.text("PREFIX", NamedTextColor.GOLD));
        List<String> strings = PrefixCommand.getPlayerPrefixMap().get(player.getUniqueId());
        if (strings == null) strings = new ArrayList<>();
        ItemStack[][] storage = new ItemStack[6][9];
        for (ItemStack[] itemStacks : storage) {
            Arrays.fill(itemStacks, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        }
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 7; j++){
                ItemStack itemStack;
                if(strings.size() > page*28 + i*7 + j){
                    String name = strings.get(page * 28 + i*7 + j);
                    itemStack = new ItemStack(Material.NAME_TAG);
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.displayName(PrefixCommand.getPrefixMap().get(name));
                    meta.lore(List.of(Component.text("클릭으로 장착", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
                    meta.getPersistentDataContainer().set(new NamespacedKey(Main.plugin, "prefix"), PersistentDataType.STRING, name);
                    itemStack.setItemMeta(meta);
                }else itemStack = new ItemStack(Material.AIR);
                storage[i+1][j+1] = itemStack;
            }
        }
        if (strings.size() > (page+1)*28){
            storage[5][5] = new ItemStack(Material.GREEN_WOOL);
        }
        if (page > 0){
            storage[5][3] = new ItemStack(Material.RED_WOOL);
        }
        ItemStack current = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = current.getItemMeta();
        itemMeta.displayName(Component.text("현재 진행도: ")
                .append(Component.text(String.format("[%d/%d]", strings.size(), PrefixCommand.getPrefixMap().size()), strings.size() == PrefixCommand.getPrefixMap().size() ? NamedTextColor.GREEN : NamedTextColor.RED, TextDecoration.BOLD))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        current.setItemMeta(itemMeta);
        storage[5][4] = current;

        ItemStack curPage = storage[0][4];
        ItemMeta pageItemMeta = curPage.getItemMeta();
        pageItemMeta.displayName(Component.text("PAGE "+page));
        curPage.setItemMeta(pageItemMeta);
        inventory.setContents(Stream.of(storage).flatMap(Stream::of).toArray(ItemStack[]::new));
        return inventory;
    }
}
