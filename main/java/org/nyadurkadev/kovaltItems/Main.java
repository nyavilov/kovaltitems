package org.nyadurkadev.kovaltItems;

import org.bukkit.plugin.java.JavaPlugin;
import org.nyadurkadev.kovaltItems.events.GardenGloves;
import org.nyadurkadev.kovaltItems.events.WaterCan;
import org.nyadurkadev.kovaltItems.item.ItemUtils;
import org.nyadurkadev.kovaltItems.recipes.Recipe;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        Recipe.registerRecipes();

        this.getCommand("getwatercan").setExecutor(new ItemUtils());
        this.getCommand("getgloves").setExecutor(new ItemUtils());
        getServer().getPluginManager().registerEvents(new WaterCan(), this);
        getServer().getPluginManager().registerEvents(new GardenGloves(), this);
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
