package org.sudaping.itemevent.prefix;

import com.google.gson.JsonParseException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrefixCommand implements CommandExecutor {

    public PrefixCommand() {
        Prefix.reload();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            if (sender instanceof Player player) {
                Inventory inventory = PrefixInventory.getInventory(player, 0);
                player.openInventory(inventory);
            }else sender.sendMessage(Component.text("명령어를 입력해 주세요!", NamedTextColor.RED));
            return true;
        }
        switch (args[0]){
            case "add" -> add(sender, args);
            case "remove" -> remove(sender, args);
            case "list" -> list(sender);
            case "set" -> set(sender, args);
            case "give" -> give(sender, args);
            case "take" -> take(sender, args);
            default -> {
                sender.sendMessage(Component.text("유효하지 않은 명령어입니다!", NamedTextColor.RED));
                return true;
            }
        }
        Prefix.reload();
        return true;
    }

    private void handlePrefix(@NotNull CommandSender sender, @NotNull String[] args, boolean isGiving) {
        if (!sender.isOp()) {
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(Component.text("칭호의 이름과 대상 플레이어를 입력해 주세요!", NamedTextColor.RED));
            return;
        }

        String key = args[1];
        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            sender.sendMessage(Component.text("플레이어가 온라인이 아닙니다!", NamedTextColor.RED));
            return;
        }

        if (isGiving) {
            if (Prefix.getPrefixMap().get(key) == null) {
                sender.sendMessage(Component.text("존재하지 않는 칭호입니다!", NamedTextColor.RED));
                return;
            }
            Prefix.givePrefix(player.getUniqueId(), key);
            sender.sendMessage(player.displayName()
                    .append(Component.text("에게 칭호 "))
                    .append(Prefix.getPrefixMap().get(key))
                    .append(Component.text("를 정상적으로 부여했습니다!")));
        } else {
            Prefix.takePrefix(player.getUniqueId(), key);
            sender.sendMessage(player.displayName()
                    .append(Component.text("에게서 칭호 "))
                    .append(Prefix.getPrefixMap().get(key))
                    .append(Component.text("를 정상적으로 박탈했습니다!")));
        }
    }

    private void take(@NotNull CommandSender sender, @NotNull String[] args) {
        handlePrefix(sender, args, false);
    }

    private void give(@NotNull CommandSender sender, @NotNull String[] args) {
        handlePrefix(sender, args, true);
    }


    private void set(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1){
            sender.sendMessage(Component.text("칭호의 이름을 입력해 주세요!", NamedTextColor.RED));
            return;
        }
        Player player;
        if (args.length == 2){
            if (sender instanceof Player p){
                player = p;
            }else {
                sender.sendMessage(Component.text("대상 플레이어를 적어주세요!", NamedTextColor.RED));
                return;
            }
        }else{
            if (!sender.isOp()){
                sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
                return;
            }
            player = Bukkit.getPlayer(args[2]);
            if (player == null){
                sender.sendMessage(Component.text("플레이어가 온라인이 아닙니다!", NamedTextColor.RED));
                return;
            }
        }
        String key = args[1];
        List<String> strings = Prefix.getPlayerPrefixMap().get(player.getUniqueId());
        if (!sender.isOp() && !key.equalsIgnoreCase("null")){
            if (!strings.contains(key)){
                sender.sendMessage(Component.text("칭호를 가지고 있지 않습니다!", NamedTextColor.RED));
                return;
            }
        }
        Prefix.setPrefix(player.getUniqueId(), key);
    }

    private void list(@NotNull CommandSender sender) {
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return;
        }
        Map<String, Component> prefixMap = Prefix.getPrefixMap();
        Component message = Component.text("현재 "+prefixMap.size()+" 개의 칭호가 있습니다: [ ");
        for (Map.Entry<String, Component> stringComponentEntry : prefixMap.entrySet()) {
            message = message
                    .append(stringComponentEntry.getValue().hoverEvent(HoverEvent.showText(Component.text(stringComponentEntry.getKey()))))
                    .appendSpace();
        }
        message = message.append(Component.text("]"));
        sender.sendMessage(message);
    }

    private void remove(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return;
        }
        if (args.length == 1){
            sender.sendMessage(Component.text("칭호의 이름을 입력해 주세요!", NamedTextColor.RED));
            return;
        }
        String key = args[1];
        if (!Prefix.getPrefixMap().containsKey(key)){
            sender.sendMessage(Component.text("존재하지 않는 칭호 이름입니다!", NamedTextColor.RED));
            return;
        }
        Prefix.removePrefix(key);
        sender.sendMessage(Component.text("칭호 ")
                .append(Component.text(key))
                .append(Component.text("가 정상적으로 삭제되었습니다!")));
    }

    private void add(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return;
        }
        if (args.length == 1){
            sender.sendMessage(Component.text("칭호의 이름을 입력해 주세요!", NamedTextColor.RED));
            return;
        }
        if (args.length == 2){
            sender.sendMessage(Component.text("칭호의 JSON을 입력해 주세요!", NamedTextColor.RED));
            return;
        }
        String key = args[1];
        Component component;
        try {
            component = GsonComponentSerializer.gson().deserialize(Arrays.stream(args).skip(2).collect(Collectors.joining(" ")));
        } catch (JsonParseException e) {
            sender.sendMessage(Component.text("올바른 JSON이 아닙니다!", NamedTextColor.RED));
            return;
        }
        if (Prefix.getPrefixMap().containsKey(key)){
            sender.sendMessage(Component.text("이미 존재하는 칭호 이름입니다!", NamedTextColor.RED));
            return;
        }
        Prefix.registerPrefix(key, component);
        sender.sendMessage(Component.text("칭호 ")
                .append(Component.text(key).hoverEvent(HoverEvent.showText(component)))
                .append(Component.text("가 정상적으로 등록되었습니다!")));
    }
}
