package lewan24.profileStaticItem;

import lewan24.profileStaticItem.Commands.StaticItemCommandProvider;
import lewan24.profileStaticItem.Commands.TabCompleters.StaticItemTabCompleter;
import lewan24.profileStaticItem.Listeners.StaticItemListener;
import lewan24.profileStaticItem.Loggers.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class main extends JavaPlugin {
    private static main instance;

    @Override
    public void onEnable() {
        Logger.info("Starting plugin...");
        instance = this;

        validateConfigAndLoadPlugin();
    }

    private void validateConfigAndLoadPlugin() {
        if(!this.getPluginMeta().getAuthors().contains("Lewan24")){
            Logger.warning("Can't load plugin. The author is invalid. Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        prepareAndLoadConfiguration();
        initializeListeners();
        initializeCommands();

        Logger.success("Plugin successfully loaded.");
    }

    private void prepareAndLoadConfiguration() {
        Logger.info("Loading configuration...");

        saveDefaultConfig();
    }

    private void initializeListeners() {
        Logger.info("Registering plugin's listeners...");

        getServer().getPluginManager().registerEvents(new StaticItemListener(getInstance()), this);
    }

    private void initializeCommands() {
        Logger.info("Registering plugin's commands...");

        Objects.requireNonNull(this.getCommand("staticItem")).setTabCompleter(new StaticItemTabCompleter());
        Objects.requireNonNull(this.getCommand("staticItem")).setExecutor(new StaticItemCommandProvider());
    }

    public static main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        Logger.info("Stopping plugin...");
    }
}
