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



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    //example {"prefix":[{"name":"sans","prefix":{"text":"sans prefix","color":"red"}}],"playerData":[{"uuid":"asdsdfaasdf",list:["sans","woops"]}]}
    /*
        prifix add/remove/list/give/take/apply
     */

}