package org.sudaping.itemevent.tabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.Prefix;

import java.util.*;

public class PrefixTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Set<String> completions = new HashSet<>();
        switch (args.length) {
            case 1 -> {
                if (sender.isOp()) completions.addAll(List.of("add", "remove", "list", "give", "take"));
                completions.add("set");
            }
            case 2 -> {
                if (sender.isOp() && List.of("remove", "set", "give", "take").contains(args[0])) {
                    completions.addAll(Prefix.getPrefixMap().keySet());
                }
                if ("set".equalsIgnoreCase(args[0]) && sender instanceof Player player) {
                    completions.addAll(Prefix.getPlayerPrefixMap().get(player.getUniqueId()));
                }
            }
            case 3 -> {
                if (sender.isOp() && List.of("remove", "set", "give", "take").contains(args[0])) {
                    completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
                }
            }
        }
        return new ArrayList<>(completions.stream().filter(s -> s.startsWith(args[args.length - 1])).sorted().toList());
    }
}
