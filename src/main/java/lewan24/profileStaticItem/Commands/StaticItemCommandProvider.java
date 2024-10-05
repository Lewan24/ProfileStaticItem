package lewan24.profileStaticItem.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class StaticItemCommandProvider implements CommandExecutor {
    private final StaticItemReloadCommand reloadCommand = new StaticItemReloadCommand();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String subcommand = args[0].toLowerCase();
        String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);

        if (subcommand.equals("reload"))
        {
            reloadCommand.onCommand(sender, command, label, subCommandArgs);
            return true;
        }
        else{
            sender.sendMessage(Component.text("Nieznana podkomenda!", NamedTextColor.RED));
            sender.sendMessage(Component.text("Możliwe opcje: [reload]", NamedTextColor.GOLD));
            return false;
        }
    }
}
