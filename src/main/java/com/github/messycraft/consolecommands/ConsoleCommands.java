package com.github.messycraft.consolecommands;

import com.github.messycraft.consolecommands.commands.ccreload;
import com.github.messycraft.consolecommands.commands.executeconsole;
import com.github.messycraft.consolecommands.commands.executeplayer;
import com.github.messycraft.consolecommands.commands.loopcmd;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConsoleCommands extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin has been loaded.");
        saveDefaultConfig();
        reloadConfig();
        getCommand("executeconsole").setExecutor(new executeconsole());
        getCommand("executeplayer").setExecutor(new executeplayer());
        getCommand("executeplayer").setTabCompleter(new executeplayer());
        getCommand("loopcmd").setExecutor(new loopcmd());
        getCommand("loopcmd").setTabCompleter(new loopcmd());
        getCommand("ccreload").setExecutor(new ccreload());
        
        // bStats support
        Metrics metrics = new Metrics(this, 14662);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin has been unloaded.");
    }
}
