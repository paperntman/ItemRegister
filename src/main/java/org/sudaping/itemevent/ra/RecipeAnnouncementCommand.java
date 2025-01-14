package org.sudaping.itemevent.ra;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.sudaping.itemevent.Archive;
import org.sudaping.itemevent.Main;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipeAnnouncementCommand implements CommandExecutor {

    private final Archive archive = Archive.load(RecipeAnnouncementCommand.class);
    public static final Map<NamespacedKey, Map<String, ItemStack>> keys = new HashMap<>();

    public RecipeAnnouncementCommand() {
        String read = archive.read();
        for (String s : read.split("\n")) {
            String[] split = s.split(",");
            NamespacedKey e = NamespacedKey.fromString(split[0]);
            ItemStack itemstack = new ItemStack(Material.AIR);

            if (e != null) {
                File parent = new File(Main.dataFolder + File.separator + "ra");
                parent.mkdirs();
                byte[] gzipFile = Main.decompressGzipFile(Main.dataFolder + File.separator + "ra" + File.separator + e.asString().replace(":", "_"));
                if (gzipFile != null) {
                    itemstack = ItemStack.deserializeBytes(gzipFile);
                }
                keys.put(e, Map.of(split[1], itemstack));
            }
        }
    }

    private void save(){
        StringBuilder builder = new StringBuilder();
        keys.forEach((namespacedKey, stringItemStackMap) ->{
            builder.append(namespacedKey.toString());
            builder.append(" ");
            stringItemStackMap.keySet().stream().findFirst().ifPresent(builder::append);
            stringItemStackMap.values().stream().findFirst().ifPresent(itemStack -> {
                if (itemStack.isEmpty()) return;
                Main.compressGzipFile(new File(Main.dataFolder+ File.separator+"ra"+File.separator+ namespacedKey.asString().replace(":", "_")), itemStack);
            });
            builder.append("\n");
        });
        archive.write(builder.toString());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return true;
        }
        if (args.length == 0 || !List.of("add", "remove", "list").contains(args[0])){
            sender.sendMessage(Component.text("명령어를 입력해 주세요!", NamedTextColor.RED));
            return true;
        }
        NamespacedKey key = null;
        if (args.length > 1){
            String regex = "^[a-z0-9._-]+:[a-z0-9._-]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(args[1]);
            if (matcher.matches()){
                key = NamespacedKey.fromString(args[1]);
            }
        }
        switch (args[0]){
            case "add":{
                if (key == null){
                    sender.sendMessage(Component.text("네임스페이스를 입력해 주세요!", NamedTextColor.RED));
                    return true;
                }
                if(Bukkit.getRecipe(key)==null){
                    sender.sendMessage(Component.text("존재하지 않는 레시피 네임스페이스입니다!", NamedTextColor.RED));
                    return true;
                }
                if(keys.containsKey(key)){
                    sender.sendMessage(Component.text("이미 등록된 레시피 네임스페이스입니다!", NamedTextColor.RED));
                    return true;
                }
                String prefix;
                if (args.length == 2){
                    sender.sendMessage(Component.text("칭호를 입력해 주세요!", NamedTextColor.RED));
                    return true;
                }
                prefix = args[2];
                ItemStack itemstack = new ItemStack(Material.AIR);
                if (sender instanceof Player player) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (!itemStack.isEmpty())
                        itemstack = itemStack;
                }
                keys.put(key, Map.of(prefix, itemstack));
                sender.sendMessage(Component.text("레시피 알림에 ", NamedTextColor.GREEN)
                        .append(Component.text( key.toString(), NamedTextColor.WHITE, TextDecoration.BOLD))
                        .append(Component.text(" 가 성공적으로 추가되었습니다!")));
                save();
                return true;
            }
            case "remove":{
                if (key == null){
                    sender.sendMessage(Component.text("네임스페이스를 입력해 주세요!", NamedTextColor.RED));
                    return true;
                }
                if (keys.keySet().stream().map(NamespacedKey::toString).noneMatch(e -> e.equalsIgnoreCase(args[1]))){
                    sender.sendMessage(Component.text("존재하지 않는 레시피 네임스페이스입니다!", NamedTextColor.RED));
                    return true;
                }
                keys.remove(NamespacedKey.fromString(args[1]));
                sender.sendMessage(Component.text("레시피 알림에서", NamedTextColor.GREEN)
                        .append(Component.text( key.toString(), NamedTextColor.WHITE, TextDecoration.BOLD))
                        .append(Component.text(" 가 성공적으로 제거되었습니다!")));
                save();
                return true;
            }
            case "list":{
                Component component;
                if(keys.isEmpty()){
                    component = Component.text("등록된 레시피 네임스페이스 알림이 없습니다!");
                }
                else{
                    component = Component.text("현재 알림 설정된 레시피 네임스페이스 알림 목록입니다.");
                }
                for (NamespacedKey namespacedKey : keys.keySet()) {
                    component = component.append(Component.text("\n")).append(Component.text(namespacedKey.toString()));
                }
                sender.sendMessage(component);
                return true;
            }
        }
        return true;
    }
}
