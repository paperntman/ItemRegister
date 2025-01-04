package org.sudaping.itemevent.cr;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.sudaping.itemevent.Main;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomRecipe {

    @Override
    public String toString() {
        return ingredients.toString() + " -> " + result.toString();
    }

    public static ArrayList<CustomRecipe> recipes = new ArrayList<>();

    private final ItemStack result;
    private final List<ItemStack> ingredients;
    private final NamespacedKey key;
    private final Recipe recipe;

    public ItemStack getResult() {
        return result;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public CustomRecipe(ItemStack item, List<ItemStack> ingredients, NamespacedKey namespacedKey) {

        this.ingredients = Collections.unmodifiableList(ingredients);

        this.result = item;
        this.key = namespacedKey;

        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, result);
        recipe.shape("abc", "def", "ghi");

        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i) == null || ingredients.get(i).getType() == Material.AIR) continue;
            recipe.setIngredient((char) ('a'+i), ingredients.get(i).getType());
        }

        this.recipe = recipe;


        recipes.add(this);
    }

    public static CustomRecipe[] getCustomRecipe(ItemStack result) {

        if (result == null || result.getType() == Material.AIR) return null;
        return recipes.stream().filter(e -> e.result.getType().equals(result.getType())).toArray(CustomRecipe[]::new);
    }

    public void save(){
        File recipeFolder = new File(Main.dataFolder+File.separator+"recipes"+File.separator + key.getKey());
        recipeFolder.mkdirs();

        for (int i = 0; i < 9; i++) {
            if (ingredients.get(i) == null) continue;
            Main.compressGzipFile(new File(recipeFolder.getAbsolutePath() + File.separator + i+".gz"), ingredients.get(i));
        }
        Main.compressGzipFile(new File(recipeFolder.getAbsolutePath() + File.separator +"result.gz"), result);
    }

    @Nullable
    public static CustomRecipe getCustomRecipeByName(String name) {
        for (CustomRecipe recipe : CustomRecipe.recipes) {
            ItemStack result = recipe.getResult();
            ItemMeta itemMeta = result.getItemMeta();
            if (itemMeta == null) continue;
            Component component = itemMeta.displayName();
            if (!(component instanceof TextComponent textComponent)) continue;
            if (textComponent.content().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static CustomRecipe getCustomRecipeByKey(String key) {
        NamespacedKey namespacedKey = new NamespacedKey(Main.plugin, key);
        CustomRecipe target = null;
        for (CustomRecipe recipe : CustomRecipe.recipes) {
            if (recipe.getKey().equals(namespacedKey)) {
                return target;
            }
        }
        return null;
    }

    public static CustomRecipe load(String key) {
        File recipeFolder = new File(Main.dataFolder+File.separator+"recipes"+File.separator + key);
        ArrayList<ItemStack> ingredients = new ArrayList<>();
        ItemStack result;

        for (int i = 0; i < 9; i++){
            File file = new File(recipeFolder.getAbsolutePath() + File.separator + i+".gz");
            if (!file.exists()) {
                ingredients.add(null);
                continue;
            }
            ingredients.add(ItemStack.deserializeBytes(Main.decompressGzipFile(file.getAbsolutePath())));
        }
        result = ItemStack.deserializeBytes(Main.decompressGzipFile(recipeFolder.getAbsolutePath() + File.separator +"result.gz"));
        return new CustomRecipe(result, ingredients, new NamespacedKey(Main.plugin, key));
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
