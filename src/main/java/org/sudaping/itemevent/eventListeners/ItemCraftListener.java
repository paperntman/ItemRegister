package org.sudaping.itemevent.eventListeners;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Recipe;
import org.sudaping.itemevent.Archive;
import org.sudaping.itemevent.commands.RecipeAnnouncement;

public class ItemCraftListener implements Listener {
    private static final Archive archive = Archive.load(ItemCraftListener.class);
    @EventHandler
    public void onCraft(CraftItemEvent event){
        if (RecipeAnnouncement.keys.stream().map(Bukkit::getRecipe).anyMatch(r -> {
            if (r == null) return false;
            return recipesEquals(event.getRecipe(), r);
        })) {
            HumanEntity human = event.getWhoClicked();
            if (human instanceof Player) {
                Player p = Bukkit.getPlayer(human.getName());
                assert p != null;

                String read = archive.read();
                JsonObject jsonObject;
                boolean announce = false;
                if (read.isEmpty()) jsonObject = new JsonObject();
                else jsonObject = JsonParser.parseString(read).getAsJsonObject();
                String key = ((Keyed) event.getRecipe()).getKey().toString();
                if (jsonObject.has(key)){
                    if(jsonObject.get(key).getAsJsonArray().asList().stream()
                            .filter(JsonElement::isJsonPrimitive)
                            .map(JsonElement::getAsJsonPrimitive)
                            .filter(JsonPrimitive::isString)
                            .map(JsonPrimitive::getAsString).noneMatch(p.getUniqueId().toString()::equalsIgnoreCase)){
                        announce = true;
                        jsonObject.get(key).getAsJsonArray().add(p.getUniqueId().toString());
                    }
                }else{
                    announce = true;
                    jsonObject.add(key, new JsonArray());
                    jsonObject.get(key).getAsJsonArray().add(p.getUniqueId().toString());
                }
                if(!announce) return;
                archive.write(jsonObject.toString());

                Bukkit.getServer().broadcast(Component.selector(p.getName())
                        .append(Component.text(" 플레이어가 "))
                        .append(event.getRecipe().getResult().displayName())
                        .append(Component.text(" 아이템을 제조했습니다!")));
            }
        }
    }

    private boolean recipesEquals(Recipe a, Recipe b) {
        if (a instanceof Keyed key_a && b instanceof Keyed key_b) {
            return key_a.getKey().equals(key_b.getKey());
        }
        return false;
    }
}
