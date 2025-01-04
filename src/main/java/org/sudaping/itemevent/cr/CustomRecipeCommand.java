package org.sudaping.itemevent.cr;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Archive;
import org.sudaping.itemevent.Main;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class CustomRecipeCommand implements CommandExecutor{

    private static final Archive archive = Archive.load(CustomRecipeCommand.class);
    public static JsonObject json = new JsonObject();

    public CustomRecipeCommand() {
        File dir = new File(Main.plugin.getDataFolder(), "recipes");
        String[] list = dir.list();
        if (list != null) {
            for (String s : list) {
                CustomRecipe load = CustomRecipe.load(s);
                Bukkit.removeRecipe(load.getKey());
                Bukkit.addRecipe(load.getRecipe());
            }
        }
        String read = archive.read();
        if (read.isEmpty()) {
            json = new JsonObject();
            json.add("blocked", new JsonArray());
            archive.write(json.toString());
        }else json = JsonParser.parseString(read).getAsJsonObject();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || !List.of("add", "remove", "view", "reload", "get").contains(args[0])){
            sender.sendMessage(Component.text("명령어를 입력해 주세요!", NamedTextColor.RED));
            return true;
        }
        if(args[0].equals("reload") && sender.isOp()){
            json = JsonParser.parseString(archive.read()).getAsJsonObject();
            return true;
        }
        if (args.length == 1){
            sender.sendMessage(Component.text("네임스페이스를 입력해 주세요!", NamedTextColor.RED));
            return true;
        }
        if (!(sender instanceof Player player)){
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
            return true;
        }
        if (args[0].equalsIgnoreCase("view")){
            view(args, player);
            return true;
        }
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return true;
        }
        if (args[0].equals("add")){
            if (player.getInventory().getItemInOffHand().getType() == Material.AIR){
                sender.sendMessage(Component.text("왼손에 제작 목표 아이템을 들어 주세요!", NamedTextColor.RED));
                return true;
            }
            if (IntStream.range(0, 9)
                    .mapToObj(player.getInventory()::getItem)
                    .toList()
                    .stream()
                    .allMatch(itemStack -> itemStack == null || itemStack.isEmpty())){
                sender.sendMessage(Component.text("핫바에 제작 재료 아이템을 넣어 주세요!", NamedTextColor.RED));
                return true;
            }
            if (CustomRecipe.recipes.stream().anyMatch(recipe -> recipe.getKey().getKey().equals(args[1]))){
                String[] argsCopy = args.clone();
                argsCopy[0] = "remove";
                remove(argsCopy, null);
            }
            add(args, player);
            player.sendMessage(Component.text("레시피 ", NamedTextColor.GREEN)
                    .append(Component.text( Main.plugin.getName()+":"+args[1], NamedTextColor.WHITE, TextDecoration.BOLD))
                    .append(Component.text(" 가 성공적으로 추가되었습니다!"))
                    .append(Component.text(" [레시피 보기]", NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/cr view "+args[1]))));
        } else if(args[0].equals("remove")){
            remove(args, player);
        } else if(args[0].equals("get")){
            get(args, player);
        }
        return true;
    }

    private void get(@NotNull String[] args, Player player) {
        NamespacedKey namespacedKey = new NamespacedKey(Main.plugin, args[1]);
        AtomicBoolean found = new AtomicBoolean(false);
        CustomRecipe.recipes.stream().filter(a -> a.getKey().equals(namespacedKey)).findFirst().ifPresent(recipe -> {
            found.set(true);
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                player.getInventory().setItem(i, recipe.getIngredients().get(i));
            }
            player.getInventory().setItemInOffHand(recipe.getResult());
        });
        if (!found.get()) {
            player.sendMessage(Component.text("존재하지 않는 레시피 네임스페이스입니다!", NamedTextColor.RED));
        }
    }

    private void view(@NotNull String[] args, Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, Component.text("DEBUG", NamedTextColor.GOLD));
        NamespacedKey namespacedKey = new NamespacedKey(Main.plugin, args[1]);
        AtomicBoolean none = new AtomicBoolean(true);
        AtomicBoolean blocked = new AtomicBoolean(false);
        CustomRecipe.recipes.stream().filter(a -> a.getKey().equals(namespacedKey)).findFirst().ifPresent(recipe -> {
            Component component = recipe.getResult().getItemMeta().displayName();
            if (component != null && json.get("blocked").getAsJsonArray().contains(new JsonPrimitive(((TextComponent) component).content()))
            && !player.isOp()){
                blocked.set(true);
                return;
            }
            ItemStack[] array = recipe.getIngredients().toArray(new ItemStack[9]);

            // 새로운 배열 생성
            ItemStack[] newArray = new ItemStack[array.length + 1];
            newArray[0] = recipe.getResult();
            System.arraycopy(array, 0, newArray, 1, array.length);

            inventory.setContents(newArray);
            player.openInventory(inventory);
            none.set(false);
        });
        if (blocked.get()){
            player.sendMessage(Component.text("볼 수 없는 레시피 네임스페이스입니다!", NamedTextColor.RED));
            return;
        }
        if(none.get()){
            player.sendMessage(Component.text("존재하지 않는 레시피 네임스페이스입니다!", NamedTextColor.RED));
        }
    }

    private void remove(@NotNull String[] args, Player player) {
        NamespacedKey namespacedKey = new NamespacedKey(Main.plugin, args[1]);

        boolean b = Bukkit.removeRecipe(namespacedKey);
        if (player != null){
            if (!b){
                player.sendMessage(Component.text("존재하지 않는 레시피 네임스페이스입니다!", NamedTextColor.RED));
            }else {
                player.sendMessage(Component.text("레시피를 성공적으로 삭제했습니다!", NamedTextColor.GREEN));
            }
        }

        CustomRecipe.recipes.removeIf(a -> a.getKey().equals(namespacedKey));
        File file = new File(Main.dataFolder + File.separator + "recipes" + File.separator + namespacedKey.getKey());
        File[] array = file.listFiles();
        if (array != null)
            Arrays.stream(array).forEach(File::delete);
        file.delete();
    }

    private static void add(@NotNull String @NotNull [] args, Player player) {
        ItemStack target = player.getInventory().getItemInOffHand().clone();
        List<ItemStack> hotbar = IntStream.range(0, 9).mapToObj(player.getInventory()::getItem).map(itemStack ->
        {
            if (itemStack != null) {
                return itemStack.clone();
            }
            return null;
        }).toList();

        NamespacedKey namespacedKey = new NamespacedKey(Main.plugin, args[1]);
        CustomRecipe e = new CustomRecipe(target, hotbar, namespacedKey);

        Bukkit.addRecipe(e.getRecipe());
        Bukkit.updateRecipes();
        e.save();
    }


}
