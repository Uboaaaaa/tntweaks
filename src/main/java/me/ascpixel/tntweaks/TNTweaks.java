package me.ascpixel.tntweaks;

import me.ascpixel.tntweaks.modules.TNTweaksModule;
import me.ascpixel.tntweaks.modules.explosivearrow.ExplosiveArrowModule;
import me.ascpixel.tntweaks.modules.fusetimemodifier.FuseTimeModifierModule;
import me.ascpixel.tntweaks.modules.tntdefuse.TntDefuseModule;
import me.ascpixel.tntweaks.modules.unstabletnt.UnstableTntModule;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * The main TNTweaks plugin class.
 */
public final class TNTweaks extends JavaPlugin {
    /**
     * The singleton instance of the plugin.
     */
    public static TNTweaks instance;

    /**
     * The logger that the plugin is using.
     */
    public Logger logger;

    /**
     * The configuration of the plugin.
     */
    public ParsedConfiguration config;

    /**
     * The localization resource.
     */
    public Localization localization;

    final TNTweaksModule[] modules = {
            new UnstableTntModule(),
            new TntDefuseModule(),
            new FuseTimeModifierModule(),
            new ExplosiveArrowModule()
    };

    @Override
    public void onEnable() {
        instance = this;

        logger = PluginLogger.getLogger("TNTweaks");
        config = new ParsedConfiguration(this);

        // Register modules
        for(TNTweaksModule module : modules){
            module.register(this, config);
        }

        // Register the main command
        PluginCommand mainCommand = getCommand("tntweaks");
        if(mainCommand == null){
            logger.severe("Could not initialize the main command! This may be caused by a recent API change. Please report this to the developer.");
        }
        else{
            final MainCommand executor = new MainCommand(this);
            mainCommand.setExecutor(executor);
            mainCommand.setTabCompleter(executor);
        }
    }

    /**
     * Reloads all loaded modules.
     * @return True if the operation has completed successfully, false otherwise.
     */
    public boolean reloadAllModules(){
        boolean result = true;
        for(TNTweaksModule module : modules){
            result = result && module.reload(config);
        }
        return result;
    }

    @Override
    public void onDisable(){
        for(TNTweaksModule module : modules){
            module.setEnabled(false);
        }
    }
}
