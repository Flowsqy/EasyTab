package fr.flowsqy.easytab.permission;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;

public class PermissionLoader {

    @Nullable
    public GroupExtractor load() {
        final RegisteredServiceProvider<LuckPerms> rsp = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (rsp == null) {
            return null;
        }
        return new GroupExtractor(rsp.getProvider());
    }

}
