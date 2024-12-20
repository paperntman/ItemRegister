package org.sudaping.itemevent;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class CustomRecipe {

    @Override
    public String toString() {
        return ingredients.toString() + " -> " + result.toString();
    }

    public static ArrayList<CustomRecipe> recipes = new ArrayList<>();
    public static Logger logger;

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
        return recipes.stream().filter(e -> e.result.isSimilar(result)).toArray(CustomRecipe[]::new);
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
