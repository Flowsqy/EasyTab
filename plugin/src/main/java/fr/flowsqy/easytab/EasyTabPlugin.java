package fr.flowsqy.easytab;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.flowsqy.easytab.config.Config;
import fr.flowsqy.easytab.config.ConfigLoader;
import fr.flowsqy.easytab.listener.ConnectionListener;
import fr.flowsqy.easytab.permission.PermissionLoader;
import fr.flowsqy.easytab.tab.ScoreboardManager;
import fr.flowsqy.easytab.tab.TabManager;

public class EasyTabPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final var configLoader = new ConfigLoader();
        final var dataFolder = getDataFolder();
        final var logger = getLogger();
        if (!configLoader.checkDataFolder(dataFolder)) {
            Bukkit.getPluginManager().disablePlugin(this);
            logger.warning("Can't write in the plugin directory. Disable the plugin");
            return;
        }
        final var config = new Config();
        config.load(configLoader, this, "config.yml");
        final var groupDatas = config.loadGroupDatas(logger);
        final var permissionLoader = new PermissionLoader();
        final var groupExtractor = permissionLoader.load();
        final var connectionListener = new ConnectionListener(groupDatas, groupExtractor, new TabManager(),
                new ScoreboardManager());
        Bukkit.getPluginManager().registerEvents(connectionListener, this);
    }

}
