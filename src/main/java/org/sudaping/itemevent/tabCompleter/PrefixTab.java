package org.sudaping.itemevent.tabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sudaping.itemevent.Prefix;
import org.sudaping.itemevent.commands.PrefixCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrefixTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1:{
                completions.add("apply");
                if (sender.isOp()){
                    completions.addAll(List.of("add", "remove", "list", "give", "take", "set"));
                }
                break;
            }
            case 2:{
                if (args[0].equalsIgnoreCase("remove")) {
                    completions.addAll(Prefix.getPrefixMap().keySet());
                    break;
                }
                if (args[0].equalsIgnoreCase("apply")) {
                    if(sender instanceof Player player){
                        completions.addAll(Prefix.getPlayerPrefixMap().get(player.getUniqueId()));
                    }
                    break;
                }
                if (List.of("give", "take", "set", "list").contains(args[0])) {
                    completions.addAll(Prefix.getPlayerPrefixMap().keySet().stream().map(Bukkit::getOfflinePlayer).map(OfflinePlayer::getName).toList());
                    completions.addAll(Arrays.stream(Bukkit.getOfflinePlayers())
                            .map(OfflinePlayer::getName).toList());
                    break;
                }
            }
            case 3:{
                var ref = new Object() {
                    List<String> strings = Prefix.getPlayerPrefixMap().get(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                };
                if (ref.strings == null) ref.strings = new ArrayList<>();
                if (List.of("take", "set").contains(args[0])) {
                    completions.addAll(ref.strings);
                    break;
                }
                if (args[0].equalsIgnoreCase("give")){
                    completions.addAll(Prefix.getPrefixMap().keySet().stream().filter(
                            s -> !ref.strings.contains(s)).toList());
                }
            }
        }
        return completions.stream().filter(s -> s.startsWith(args[args.length - 1])).collect(Collectors.toList());
    }
}
