package fr.flowsqy.easytab;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.flowsqy.easytab.config.Config;
import fr.flowsqy.easytab.config.ConfigLoader;
import fr.flowsqy.easytab.group.GroupData;
import fr.flowsqy.easytab.permission.PermissionLoader;

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
    }

}
