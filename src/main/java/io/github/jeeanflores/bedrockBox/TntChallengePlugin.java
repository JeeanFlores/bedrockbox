package io.github.jeeanflores.bedrockBox;

import io.github.jeeanflores.bedrockBox.commands.TntChallengeCommand;
import io.github.jeeanflores.bedrockBox.commands.completer.TntChallengeCommandCompleter;
import io.github.jeeanflores.bedrockBox.listeners.BlockListener;
import io.github.jeeanflores.bedrockBox.listeners.EntityListener;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TntChallengePlugin extends JavaPlugin {

    @Getter
    private static TntChallengePlugin instance;

    @Override
    public void onEnable() {

        instance = this;

        PluginCommand tntChallengeCommand = this.getCommand("tntchallenge");
        tntChallengeCommand.setExecutor(new TntChallengeCommand());
        tntChallengeCommand.setTabCompleter(new TntChallengeCommandCompleter());

        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);

        World world = Bukkit.getWorld("world");

        world.setTime(6000L);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doWeatherCycle", "false");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
