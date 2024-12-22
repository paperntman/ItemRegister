package org.sudaping.itemevent.commands;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.Archive;
import org.sudaping.itemevent.PrefixInventory;

import java.util.*;
import java.util.stream.Collectors;

public class PrefixCommand implements CommandExecutor {

    private static final Archive archive = Archive.load(PrefixCommand.class);
    private static final Map<String, Component> prefixMap = new HashMap<>();
    private static final Map<UUID, List<String>> playerPrefixMap = new HashMap<>();

    public static Map<String, Component> getPrefixMap() {
        return prefixMap;
    }

    public static Map<UUID, List<String>> getPlayerPrefixMap() {
        return playerPrefixMap;
    }

    //example {"prefix":[{"name":"sans","prefix":{"text":"sans prefix","color":"red"}}],"playerData":[{"uuid":"asdsdfaasdf",list:["sans","woops"]}]}

    public static void givePrefix(Player player, String prefixName) {

    }

    public PrefixCommand() {
        String read = archive.read();
        if (read.isEmpty()) {
            read = "{prefix:[],playerData:[]}";
            save();
        }
        JsonObject jsonObject = new Gson().fromJson(read, JsonObject.class);
        for (JsonElement jsonElement : jsonObject.get("prefix").getAsJsonArray()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String name = asJsonObject.get("name").getAsString();
            JsonElement prefix = asJsonObject.get("prefix");
            prefixMap.put(name, GsonComponentSerializer.gson().deserializeFromTree(prefix));
        }
        for (JsonElement jsonElement : jsonObject.get("playerData").getAsJsonArray()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            UUID uuid = UUID.fromString(asJsonObject.get("uuid").getAsString());
            List<String> list = asJsonObject.get("list").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList();
            playerPrefixMap.put(uuid, list);
        }
    }

    private static void save() {
        JsonObject jsonObject = new JsonObject();
        JsonArray prefixArr = new JsonArray();
        for (Map.Entry<String, Component> stringComponentEntry : prefixMap.entrySet()) {
            String prefix = stringComponentEntry.getKey();
            Component component = stringComponentEntry.getValue();
            JsonObject prefixObj = new JsonObject();
            prefixObj.addProperty("name", prefix);
            prefixObj.add("prefix", GsonComponentSerializer.gson().serializeToTree(component));
            prefixArr.add(prefixObj);
        }
        jsonObject.add("prefix", prefixArr);
        JsonArray playerDataObj = getJsonElements();
        jsonObject.add("playerData", playerDataObj);
        archive.write(jsonObject.toString());
    }

    private static @NotNull JsonArray getJsonElements() {
        JsonArray playerDataObj = new JsonArray();
        for (Map.Entry<UUID, List<String>> stringListEntry : playerPrefixMap.entrySet()) {
            UUID uuid = stringListEntry.getKey();
            List<String> list = stringListEntry.getValue();
            JsonObject playerData = new JsonObject();
            playerData.addProperty("uuid", uuid.toString());
            JsonArray listPrefix = new JsonArray();
            for (String prefix : list) {
                listPrefix.add(prefix);
            }
            playerData.add("list", listPrefix);
            playerDataObj.add(playerData);
        }
        return playerDataObj;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 ) {
            if (!(sender instanceof Player player)){
                sender.sendMessage(Component.text("플레이어만 사용할 수 있습니다!", NamedTextColor.RED));
                return true;
            }
            Inventory inventory = PrefixInventory.getInventory(player, 0);
            player.openInventory(inventory);
            return true;
        }

        if (args[0].equalsIgnoreCase("apply")){
            Player player = (Player) sender;
            if (args.length == 1){
                player.displayName(Component.selector(player.getName()));
                player.playerListName(player.displayName());
                sender.sendMessage(Component.selector(player.getName())
                        .append(Component.text("의 칭호를 제거했습니다!")));
                return true;
            }
            if(playerPrefixMap.get(player.getUniqueId()) == null || !playerPrefixMap.get(player.getUniqueId()).contains(args[1])){
                sender.sendMessage(Component.text("해당 칭호를 소유하고 있지 않습니다!", NamedTextColor.RED));
                return true;
            }
            Component component = prefixMap.get(args[1]);
            player.displayName(component.append(Component.text(player.getName())));
            player.playerListName(player.displayName());
            sender.sendMessage(Component.text("칭호를 ").append(component).append(Component.text("로 설정하였습니다!")));
            return true;
        }
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED));
            return true;
        }
        if (args[0].equalsIgnoreCase("add")){
            String name = args[1];
            String collect = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
            try {
                JsonElement prefix = JsonParser.parseString(collect);
                Component value = GsonComponentSerializer.gson().deserializeFromTree(prefix);
                prefixMap.put(name, value);
                sender.sendMessage(Component.text("칭호 ").append(value).append(Component.text("를 생성하였습니다!")));
                save();
            } catch (Exception e) {
                sender.sendMessage(Component.text("올바른 JSON 형식이 아닙니다!", NamedTextColor.RED));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")){
            String name = args[1];
            sender.sendMessage(Component.text("칭호 ").append(prefixMap.get(args[1])).append(Component.text("를 삭제하였습니다!")));
            prefixMap.remove(name);
            for (Map.Entry<UUID, List<String>> uuidListEntry : playerPrefixMap.entrySet()) {
                ArrayList<String> arrayList = new ArrayList<>(uuidListEntry.getValue());
                arrayList.remove(name);
                playerPrefixMap.put(uuidListEntry.getKey(), arrayList);
            }
            save();
            return true;
        }
        if (args[0].equalsIgnoreCase("list")){
            if (args.length == 1){
                if (prefixMap.isEmpty()) sender.sendMessage("등록된 칭호가 없습니다!");
                else prefixMap.forEach((name, prefix) -> sender.sendMessage(Component.text(name+" : ").append(prefix)));
            }else {
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                List<String> strings = playerPrefixMap.get(player.getUniqueId());
                if (strings != null && !strings.isEmpty()) {
                    Component component;
                    if (Bukkit.getPlayer(args[1]) == null){
                        component = Component.text(args[1]);
                    }else {
                        component = Component.selector(args[1]);
                    }
                    component = component.append(Component.text("의 칭호: "));
                    for (String string : strings) {
                        component = component.append(Component.text(string).hoverEvent(HoverEvent.showText(prefixMap.get(string)))).append(Component.space());
                    }
                    sender.sendMessage(component);
                }else {
                    sender.sendMessage(Component.text(player.getUniqueId()+"이 가진 칭호가 없습니다!"));
                }
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("give")){
            UUID uniqueId = prefixCheck(sender, args);
            if (uniqueId == null) return true;

            Component component;
            if (Bukkit.getPlayer(uniqueId) == null){
                component = Component.text(uniqueId.toString());
            }else {
                component = Component.selector(uniqueId.toString());
            }

            List<String> stringList = playerPrefixMap.get(uniqueId);
            List<String> strings = new LinkedList<>();
            if(stringList != null) strings.addAll(stringList);
            if(strings.contains(args[2])){
                sender.sendMessage(component
                        .append(Component.text("은 칭호"))
                        .append(prefixMap.get(args[2]))
                        .append(Component.text("를 이미 가지고 있습니다!")));
                return true;
            }
            strings.add(args[2]);
            playerPrefixMap.put(uniqueId, strings);
            save();
            sender.sendMessage(component
                    .append(Component.text("에게 칭호 "))
                    .append(prefixMap.get(args[2]))
                    .append(Component.text("를 지급하였습니다!")));
            if (Bukkit.getPlayer(uniqueId) != null){
                Bukkit.getPlayer(uniqueId).sendMessage(
                        Component.text("칭호 ")
                                .append(prefixMap.get(args[2]))
                                .append(Component.text("을 획득했습니다!")));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("take")){
            Result result = getResult(sender, args);
            if (result == null) return true;

            List<String> strings = new ArrayList<>(playerPrefixMap.get(result.uniqueId()));
            if(!strings.contains(args[2])){
                sender.sendMessage(result.component()
                        .append(Component.text("은 칭호 "))
                        .append(prefixMap.get(args[2]))
                        .append(Component.text("를 가지고 있지 않습니다!")));
                return true;
            }
            strings.remove(args[2]);
            playerPrefixMap.put(result.uniqueId(), strings);
            save();
            sender.sendMessage(result.component()
                    .append(Component.text("에게서 칭호 "))
                    .append(prefixMap.get(args[2]))
                    .append(Component.text("를 삭제하였습니다!")));
            return true;
        }
        if (args[0].equalsIgnoreCase("set")){
            if (args.length == 1){
                sender.sendMessage("플레이어를 적어주세요!");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(Component.text("해당 플레이어가 온라인이 아닙니다!", NamedTextColor.RED));
                return true;
            }
            if (args.length == 2){
                player.displayName(Component.selector(player.getName()));
                player.playerListName(player.displayName());


                sender.sendMessage(Component.selector(player.getName())
                        .append(Component.text("의 칭호를 제거했습니다!")));
                return true;
            }
            if(!prefixMap.containsKey(args[2])){
                sender.sendMessage(Component.text("해당하는 칭호가 존재하지 않습니다!", NamedTextColor.RED));
                return true;
            }
            Component component = prefixMap.get(args[2]);
            player.displayName(component.append(Component.selector(player.getName())));
            player.playerListName(player.displayName());

            sender.sendMessage(Component.selector(player.getName())
                    .append(Component.text("의 칭호를 "))
                    .append(component)
                    .append(Component.text("로 설정했습니다!")));
        }
        return true;
    }

    private static @Nullable Result getResult(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        UUID uniqueId = prefixCheck(sender, args);
        if (uniqueId == null) return null;

        Component component;
        if (Bukkit.getPlayer(uniqueId) == null){
            component = Component.text(uniqueId.toString());
        }else {
            component = Component.selector(uniqueId.toString());
        }
        return new Result(uniqueId, component);
    }

    private record Result(UUID uniqueId, Component component) {
    }

    private static UUID prefixCheck(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length == 1){
            sender.sendMessage("플레이어를 적어주세요!");
            return null;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        UUID uniqueId = player.getUniqueId();
        if (args.length == 2){
            sender.sendMessage("칭호를 적어주세요!");
            return null;
        }
        if(!prefixMap.containsKey(args[2])){
            sender.sendMessage(Component.text("해당하는 칭호가 존재하지 않습니다!", NamedTextColor.RED));
            return null;
        }
        return uniqueId;
    }
}
