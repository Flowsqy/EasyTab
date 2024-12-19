package fr.flowsqy.easytab.permission;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import net.luckperms.api.LuckPerms;

public class PermissionLoader {

    @NotNull
    public Optional<LuckPerms> load() {
        final RegisteredServiceProvider<LuckPerms> rsp = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (rsp == null) {
            return Optional.empty();
        }
        return Optional.of(rsp.getProvider());
    }

}
