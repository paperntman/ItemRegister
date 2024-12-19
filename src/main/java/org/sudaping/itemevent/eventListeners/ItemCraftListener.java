package org.sudaping.itemevent.eventListeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Recipe;
import org.sudaping.itemevent.Main;
import org.sudaping.itemevent.commands.RecipeAnnouncement;

public class ItemCraftListener implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent event){
        System.out.println("# "+event.getRecipe());
        System.out.println(RecipeAnnouncement.keys);
        if (RecipeAnnouncement.keys.stream().map(Bukkit::getRecipe).anyMatch(recipe -> {
            System.out.println(recipe);
            if (recipe == null) return false;
            return recipe.equals(event.getRecipe());
        })) {
            HumanEntity human = event.getWhoClicked();
            if (human instanceof Player) {
                Player p = Bukkit.getPlayer(human.getName());
                assert p != null;
                Bukkit.getServer().broadcast(Component.selector(p.getName())
                        .append(Component.text(" 플레이어가 "))
                        .append(event.getRecipe().getResult().displayName())
                        .append(Component.text(" 아이템을 제조했습니다!")));
            }
        }
    }

    private boolean recipesEquals(Recipe a, Recipe b) {
        return a.getResult().equals(b.getResult()) && 
    }
}
