package org.sudaping.flying;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin{

    @Override
    public void onEnable() {
        PluginCommand command = getCommand("itemevent");
        assert command != null;
        command.setExecutor(new Registration());
        getServer().getPluginManager().registerEvents(new ItemEventHandler(), this);
        ItemEventHandler.initialize();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
