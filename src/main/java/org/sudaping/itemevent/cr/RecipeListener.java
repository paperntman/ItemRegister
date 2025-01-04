package org.sudaping.itemevent.cr;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RecipeListener implements Listener {
    @EventHandler
    public void onItemCraftEvent(PrepareItemCraftEvent event) {

        if (event.getInventory().getResult() == null || event.getInventory().getResult().getType() == Material.AIR) return;

        CustomRecipe[] recipes = CustomRecipe.getCustomRecipe(event.getInventory().getResult());


        if (recipes == null) return;
        for (CustomRecipe recipe : recipes) {
            boolean flag = true;
            @Nullable ItemStack[] matrix = event.getInventory().getMatrix();

            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i] == null) continue;
                if (Objects.requireNonNull(matrix[i]).getType() == Material.AIR) continue;
                if (recipe.getIngredients().get(i) == null) continue;
                if (!recipe.getIngredients().get(i).isSimilar(matrix[i])) {
                    flag = false;
                    break;
                }
            }

            if (!flag) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
            else {
                event.getInventory().setResult(recipe.getResult());
                break;
            }
        }
    }
}
