package lewan24.profileStaticItem.Commands;

import lewan24.profileStaticItem.Loggers.Logger;
import lewan24.profileStaticItem.main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class StaticItemReloadCommand implements CommandExecutor {
    FileConfiguration config = main.getInstance().getConfig();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String permission = config.getString("config.permissions.reload");

        if (permission != null && (sender.hasPermission(permission) || sender.isOp())) {
            main.getInstance().reloadConfig();
            Logger.success("Successfully reloaded config!");
            sender.sendMessage(Component.text("Reloaded config!", NamedTextColor.GREEN));
            return true;
        }

        Logger.warning("User '" + sender.getName() + "' does not have permission to reload config!");
        sender.sendMessage(Component.text("You don't have permission to reload config!", NamedTextColor.RED));
        return false;
    }
}
