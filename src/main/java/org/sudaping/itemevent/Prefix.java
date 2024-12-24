package org.sudaping.itemevent;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.units.qual.C;
import org.sudaping.itemevent.commands.PrefixCommand;

import java.util.*;

public class Prefix {

    //example {"prefix":[{"key":"sans","component":{"text":"sans prefix","color":"red"}}],"playerData":[{"uuid":"asdsdfaasdf",list:["sans","woops"], current: "sans"}]}
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
        return Collections.unmodifiableMap(componentMap);
    }

    public static Map<UUID, List<String>> getPlayerPrefixMap() {
        Map<UUID, List<String>> componentMap = new HashMap<>();
        JsonArray prefixes = jsonObject.get("playerData").getAsJsonArray();
        for (JsonElement data : prefixes) {
            JsonObject part = data.getAsJsonObject();
            List<String> prefixList = new ArrayList<>();

            UUID key = UUID.fromString(part.get("uuid").getAsString());
            for (JsonElement element : part.get("list").getAsJsonArray())
                prefixes.add(element.getAsString());

            componentMap.put(key, prefixList);
        }
        return Collections.unmodifiableMap(componentMap);
    }

    private static void save(){
        archive.write(jsonObject.getAsString());
    }

    private static void load(){
        jsonObject = JsonParser.parseString(archive.read()).getAsJsonObject();
    }



}
