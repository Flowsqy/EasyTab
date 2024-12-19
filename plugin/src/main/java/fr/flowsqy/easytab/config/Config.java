package fr.flowsqy.easytab.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.flowsqy.easytab.group.GroupData;

public class Config {

    private YamlConfiguration configuration;

    public void load(@NotNull ConfigLoader configLoader, @NotNull JavaPlugin javaPlugin, @NotNull String fileName) {
        configuration = YamlConfiguration.loadConfiguration(configLoader.initFile(javaPlugin.getDataFolder(),
                Objects.requireNonNull(javaPlugin.getResource(fileName)), fileName));
    }

    @NotNull
    public Map<String, GroupData> loadGroupDatas(@NotNull Logger logger) {
        final Map<String, GroupData> groupDatas = new HashMap<>();
        for (String key : configuration.getKeys(false)) {
            final var section = configuration.getConfigurationSection(key);
            if (section == null) {
                logger.warning("'" + key + "' is not a section");
                continue;
            }
            final var groupData = loadGroupData(logger, key, section);
            if (groupData == null) {
                continue;
            }
            groupDatas.put(key, groupData);
        }
        return groupDatas;
    }

    @Nullable
    private GroupData loadGroupData(@NotNull Logger logger, @NotNull String groupName,
            @NotNull ConfigurationSection groupSection) {
        final var priority = groupSection.getInt("priority");
        final var color = loadColor(groupSection);
        final var rawPrefix = groupSection.getString("prefix");
        final var rawSuffix = groupSection.getString("suffix");
        final var prefix = rawPrefix == null ? null : ChatColor.translateAlternateColorCodes('&', rawPrefix);
        final var suffix = rawSuffix == null ? null : ChatColor.translateAlternateColorCodes('&', rawSuffix);
        final var groupData = new GroupData(groupName, priority, color, prefix, suffix);
        return groupData;
    }

    @Nullable
    private ChatColor loadColor(@NotNull ConfigurationSection groupSection) {
        final var colorName = groupSection.getString("color");
        if (colorName == null) {
            return null;
        }
        for (var color : ChatColor.values()) {
            if (color.name().equalsIgnoreCase(colorName)) {
                return color;
            }
        }
        return null;
    }

}
