package org.sudaping.itemevent.tabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegistrationTab implements @Nullable TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return switch (args.length) {
            case 1 -> Stream.of("register").filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            case 2 -> Stream.of("death", "fly5m").filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
            default -> null;
        };
    }
}
