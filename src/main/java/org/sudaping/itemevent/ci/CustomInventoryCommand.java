package org.sudaping.itemevent.ci;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.Main;

import java.io.File;
import java.util.*;

public class CustomInventoryCommand implements CommandExecutor {

    public static final Map<String, Inventory> inventoryMap = new HashMap<>();
    public static final Map<Player, String> modifying = new HashMap<>();

    public CustomInventoryCommand() {
        load();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Component.text("명령어를 적어 주세요!", NamedTextColor.RED));
            return true;
        }
        switch (args[0]) {
            case "create" -> create(sender, args);
            case "list" -> list(sender);
            case "remove" -> remove(sender, args);
            case "modify" -> modify(sender, args);
            case "view" -> view(sender, args);
        }
        return true;
    }

    private void view(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("인벤토리의 이름을 적어 주세요!", NamedTextColor.RED));
            return;
        }
        if (sender instanceof Player player) {
            Inventory inventory = inventoryMap.get(args[1]);
            if (inventory != null) {
                player.openInventory(inventory);
            }else
                sender.sendMessage(Component.text("인벤토리를 찾을 수 없습니다!", NamedTextColor.RED));
        }else
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
    }


    private void create(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("인벤토리의 이름을 적어 주세요!", NamedTextColor.RED));
            return;
        }
        String name = args[1];
        if (inventoryMap.containsKey(name)) {
            sender.sendMessage(Component.text("이미 존재하는 인벤토리입니다!", NamedTextColor.RED));
            return;
        }
        inventoryMap.put(name, Bukkit.createInventory(null, 54, Component.text("GUI", NamedTextColor.GOLD)));
        save();
        sender.sendMessage(Component.text(name+"의 이름을 가진 인벤토리를 제작했습니다!"));
    }

    private void list(CommandSender sender) {
        sender.sendMessage(inventoryMap.keySet().toString());
    }

    private void remove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("인벤토리의 이름을 적어 주세요!", NamedTextColor.RED));
            return;
        }
        inventoryMap.remove(args[1]);
        sender.sendMessage(Component.text(args[1]+"의 이름을 가진 인벤토리를 제거했습니다!"));
    }

    private void modify(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("인벤토리의 이름을 적어 주세요!", NamedTextColor.RED));
            return;
        }
        if (sender instanceof Player player) {
            Inventory inventory = inventoryMap.get(args[1]);
            if (inventory != null) {
                player.openInventory(inventory);
                modifying.put(player, args[1]);
            }else
                sender.sendMessage(Component.text("인벤토리를 찾을 수 없습니다!", NamedTextColor.RED));
        }else
            sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));

    }

    public static void save(){
        File path = new File(Main.dataFolder+File.separator+"Inventory");
        cleanDir(path);
        path.mkdir();
        for (Map.Entry<String, Inventory> stringInventoryEntry : inventoryMap.entrySet()) {
            String name = stringInventoryEntry.getKey();
            Inventory inventory = stringInventoryEntry.getValue();

            File parent = new File(path.getAbsolutePath() + File.separator + name);
            parent.mkdir();
            @Nullable ItemStack[] contents = inventory.getContents();
            for (int i = 0; i < contents.length; i++) {
                File target = new File(parent.getAbsolutePath() + File.separator + i);
                ItemStack content = contents[i];
                if(content == null) continue;
                if(content.getType().equals(Material.AIR)) continue;
                Main.compressGzipFile(target, content);
            }
        }
    }

    public static void load(){
        File path = new File(Main.dataFolder+File.separator+"Inventory");
        File[] files = path.listFiles();
        if(files == null) return;
        for (File file : files) {
            String name = file.getName();
            File[] children = file.listFiles();
            ItemStack[] contents = new ItemStack[54];
            if(children == null) continue;
            for (File child : children) {
                contents[Integer.parseInt(child.getName())] = ItemStack.deserializeBytes(Main.decompressGzipFile(child.getAbsolutePath()));
            }
            Inventory inventory = Bukkit.createInventory(null, 54, Component.text("GUI", NamedTextColor.GOLD));
            inventory.setContents(contents);
            inventoryMap.put(name, inventory);
        }
    }

    private static void cleanDir(File file){
        if(!file.exists()) return;
        if (file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null) {
                for (File f : files){
                    cleanDir(f);
                }
            }
        }
        file.delete();
    }
}
