package lewan24.profileStaticItem.Commands.TabCompleters;

import lewan24.profileStaticItem.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class StaticItemTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FileConfiguration config = main.getInstance().getConfig();
        String permission = config.getString("config.permissions.main");

        if (permission == null || (!sender.hasPermission(permission) || !sender.isOp()))
            return null;

        List<String> suggestions = List.of("reload");

        if (args.length != 1)
            return null;

        List<String> completions = new ArrayList<>();
        for (String s : suggestions)
            if (s.toLowerCase().startsWith(args[0].toLowerCase()))
                completions.add(s);

        return completions;

    }
}
