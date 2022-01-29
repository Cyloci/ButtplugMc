package com.psiloclast.buttplugmc;

import com.psiloclast.buttplugmc.commands.AddToyCommand;
import com.psiloclast.buttplugmc.commands.VibrateCommand;
import com.psiloclast.buttplugmc.listeners.VibrateOnBlockBreak;
import com.psiloclast.buttplugmc.listeners.VibrateOnDamage;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.jorel.commandapi.arguments.DoubleArgument;
import io.buttplug.ButtplugClient;
import java.util.HashMap;
import java.util.UUID;

public class ButtplugMc extends JavaPlugin {

  private HashMap<UUID, ButtplugClient> buttplugClients;

  @Override
  public void onLoad() {
    this.buttplugClients = new HashMap<>();

    CommandAPI.onLoad(new CommandAPIConfig());

    AddToyCommand addToyCommand = new AddToyCommand(this, this.buttplugClients);
    VibrateCommand vibrateCommand = new VibrateCommand(this.buttplugClients);

    new CommandAPICommand("add-toy")
        .executesPlayer((player, args) -> {
          addToyCommand.handleCommand(player);
        })
        .register();

    new CommandAPICommand("vibrate")
        .withArguments(new DoubleArgument("level"))
        .executesPlayer((player, args) -> {
          double level = (double) args[0];
          vibrateCommand.handleCommand(player, level);
        })
        .executesProxy((proxy, args) -> {
          if (!(proxy.getCallee() instanceof Player)) {
            return;
          }
          Player player = (Player) proxy.getCallee();
          double level = (double) args[0];
          vibrateCommand.handleCommand(player, level);
        })
        .register();
  }

  @Override
  public void onEnable() {
    CommandAPI.onEnable(this);

    getServer().getPluginManager().registerEvents(new VibrateOnBlockBreak(this, buttplugClients), this);
    getServer().getPluginManager().registerEvents(new VibrateOnDamage(this, buttplugClients), this);

    getConfig().options().copyDefaults(true);
    saveConfig();
    getLogger().info("Buttplug MC is enabled.");
  }

  @Override
  public void onDisable() {
    buttplugClients.values().forEach(buttplugClient -> {
      buttplugClient.stopAllDevices();
      buttplugClient.close();
    });
    saveConfig();
    getLogger().info("Buttplug MC is disabled.");
  }
}
