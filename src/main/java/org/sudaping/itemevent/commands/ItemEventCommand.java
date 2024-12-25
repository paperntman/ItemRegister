package org.sudaping.itemevent.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemEventCommand implements CommandExecutor {

    public static Map<ItemStack, String> componentMap = new HashMap<>();
    public static File classpath;

    public ItemEventCommand(){
        classpath = new File(Main.dataFolder + File.separator + "components");
        load();
    }

    //itemevent command
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage(Component.text("권한이 없습니다!").color(NamedTextColor.RED));
            return true;
        }
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!").color(NamedTextColor.RED));
            return true;
        }
        if (args.length == 0){
            sender.sendMessage(componentMap.toString());
            sender.sendMessage(Component.text("명령어를 입력해 주세요!").color(NamedTextColor.RED));
            return true;
        }
        register(args, player);
        return true;
    }

    private static void register(@NotNull String @NotNull [] args, Player player) {
        ItemStack targetItemStack = player.getInventory().getItemInMainHand().asOne();
        if (targetItemStack.getItemMeta() == null || targetItemStack.getItemMeta().displayName() == null){
            player.sendMessage(Component.text("아이템에 커스텀 이름이 없습니다!").color(NamedTextColor.RED));
            return;
        }
        if (args.length == 0){
            componentMap.remove(targetItemStack);
            player.sendMessage(targetItemStack.displayName()
                    .append(Component.text("에 지정된 태그를 삭제했습니다!", NamedTextColor.GREEN)));
        }else{
            ItemStack toRemove = null;
            for (ItemStack itemStack : componentMap.keySet()) {
                if (componentMap.get(itemStack).equalsIgnoreCase(args[0])){
                    toRemove = itemStack;
                }
            }
            if (toRemove != null) componentMap.remove(toRemove);
            componentMap.put(targetItemStack, args[0]);
            player.sendMessage(targetItemStack.displayName()
                    .append(Component.text("에 ", NamedTextColor.GREEN))
                    .append(Component.text(args[0], NamedTextColor.GOLD))
                    .append(Component.text(" 태그를 부여했습니다!", NamedTextColor.GREEN)));
        }
        save();
    }

    public static void save(){
        classpath.mkdir();
        File[] files = classpath.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        for (Map.Entry<ItemStack, String> componentStringEntry : componentMap.entrySet()) {
            ItemStack key = componentStringEntry.getKey();
            String value = componentStringEntry.getValue();
            File path = new File(classpath.getAbsolutePath() + File.separator + value);
            key.getItemMeta().displayName();
            Main.compressGzipFile(path, key);
        }
    }

    public static void load(){
        File[] files = classpath.listFiles();
        if (files == null) return;
        for (File listFile : files) {
            componentMap.put(ItemStack.deserializeBytes(Main.decompressGzipFile(listFile.getAbsolutePath())), listFile.getName());
        }
    }


}
