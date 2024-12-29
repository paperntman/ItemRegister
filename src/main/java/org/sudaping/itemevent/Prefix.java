package org.sudaping.itemevent;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.Collectors;

public class Prefix {

    //example {"prefix":[{"key":"sans","component":{"text":"sans prefix","color":"red"}}],"playerData":{"asdsdfaasdf"{list:["sans","woops"], current: "sans"}}}
    private static final Archive archive = Archive.load(Prefix.class);
    private static JsonObject jsonObject;

    public static Map<String, Component> getPrefixMap() {
        Map<String, Component> componentMap = new HashMap<>();
        JsonArray prefixes = jsonObject.get("prefix").getAsJsonArray();
        for (JsonElement prefix : prefixes) {
            JsonObject part = prefix.getAsJsonObject();
            String key = part.get("key").getAsString();
            Component value = GsonComponentSerializer.gson().deserializeFromTree(part.get("component"));
            componentMap.put(key, value);
        }
        return componentMap;
    }

    public static Map<UUID, List<String>> getPlayerPrefixMap() {
        Map<UUID, List<String>> componentMap = new HashMap<>();
        JsonObject prefixes = jsonObject.get("playerData").getAsJsonObject();
        for (String s : prefixes.keySet()) {
            UUID key = UUID.fromString(s);
            List<String> values = prefixes.get(s).getAsJsonObject().get("list").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList();
            componentMap.put(key, values);
        }
        return componentMap;
    }

    private static void save(){
        archive.write(jsonObject.toString());
    }

    private static void load(){
        String read = archive.read();
        if (!read.isEmpty()){
            jsonObject = JsonParser.parseString(read).getAsJsonObject();
        }else {
            JsonArray prefixes = new JsonArray();
            JsonObject nullPrefix = new JsonObject();
            nullPrefix.add("key", new JsonPrimitive("null"));
            nullPrefix.add("component", GsonComponentSerializer.gson().serializeToTree(Component.empty()));
            prefixes.add(nullPrefix);
            jsonObject = new JsonObject();
            jsonObject.add("prefix", prefixes);
            jsonObject.add("playerData", new JsonObject());
        }
    }

    public static void registerPrefix(String key, Component prefix) {
        JsonArray jsonArray = jsonObject.get("prefix").getAsJsonArray();
        JsonObject newPrefix = new JsonObject();
        newPrefix.addProperty("key", key);
        newPrefix.add("component", GsonComponentSerializer.gson().serializeToTree(prefix));
        jsonArray.add(newPrefix);
        save();
    }

    public static void removePrefix(String key) {
        JsonArray jsonArray = jsonObject.get("prefix").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject prefix = jsonElement.getAsJsonObject();
            if (prefix.get("key").getAsString().equals(key)) {
                jsonArray.remove(jsonElement);
            }
        }
        JsonObject playerData = jsonObject.get("playerData").getAsJsonObject();
        playerData.asMap().forEach((k, v) -> {
            JsonObject data = v.getAsJsonObject();
            data.get("list").getAsJsonArray().remove(new JsonPrimitive(key));
            if (data.get("current").getAsString().equals(key)) data.add("current", new JsonPrimitive("null"));
        });
        save();
    }

    private static JsonObject getOrCreatePlayerData(UUID uuid) {
        JsonObject playerData = jsonObject.get("playerData").getAsJsonObject();
        JsonElement dataElement = playerData.get(uuid.toString());
        if (dataElement == null) {
            dataElement = new JsonObject();
            JsonArray prefixList = new JsonArray();
            prefixList.add("null");
            dataElement.getAsJsonObject().add("list", prefixList);
            dataElement.getAsJsonObject().addProperty("current", "null");

            playerData.add(uuid.toString(), dataElement);
        }
        jsonObject.add("playerData", playerData);
        return dataElement.getAsJsonObject();
    }

    private static JsonArray getOrCreateList(JsonObject dataElement) {
        JsonElement listElement = dataElement.get("list");
        if (listElement == null) {
            listElement = new JsonArray();
            dataElement.add("list", listElement);
        }
        return listElement.getAsJsonArray();
    }

    public static boolean givePrefix(UUID uuid, String key) {
        JsonObject dataElement = getOrCreatePlayerData(uuid);
        JsonArray listElement = getOrCreateList(dataElement);
        if (listElement.contains(new JsonPrimitive(key))){
            return false;
        }else{
            listElement.add(key);
        }
        save();
        return true;
    }

    public static boolean takePrefix(UUID uuid, String key) {
        JsonObject dataElement = getOrCreatePlayerData(uuid);
        JsonArray listElement = getOrCreateList(dataElement);
        if (listElement.contains(new JsonPrimitive(key))){
            listElement.remove(new JsonPrimitive(key));
        }else{
            return false;
        }
        save();
        return true;
    }

    public static void setPrefix(UUID uuid, String key) {
        JsonObject dataElement = getOrCreatePlayerData(uuid);
        dataElement.addProperty("current", key);
        save();
    }

    public static void reload(){
        load();
        Bukkit.getOnlinePlayers().forEach(player -> {
            JsonObject playerData = getOrCreatePlayerData(player.getUniqueId());
            JsonElement jsonElement = playerData.get("current");
            if (jsonElement.getAsString().equals("null")){
                player.displayName(Component.text(player.getName()));
                player.playerListName(player.displayName());
            }else{
                String current = jsonElement.getAsString();
                Component prefix = getPrefixMap().get(current);

                player.displayName(prefix.append(Component.text(player.getName())));
                player.playerListName(player.displayName());
            }
        });
    }




}
