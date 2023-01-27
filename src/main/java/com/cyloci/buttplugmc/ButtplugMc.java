package com.cyloci.buttplugmc;

import com.cyloci.buttplugmc.commands.VibrateCommand;
import com.cyloci.buttplugmc.listeners.ConnectToIntifaceOnLogin;
import com.cyloci.buttplugmc.listeners.VibrateOnBlockBreak;
import com.cyloci.buttplugmc.listeners.VibrateOnDamage;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.jorel.commandapi.arguments.DoubleArgument;

public class ButtplugMc extends JavaPlugin {

  private ButtplugClientManager clientManager;

  @Override
  public void onLoad() {
    this.clientManager = new ButtplugClientManager(this);
    CommandAPI.onLoad(new CommandAPIConfig());

    VibrateCommand vibrateCommand = new VibrateCommand(this.clientManager);

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

    getServer().getPluginManager().registerEvents(new ConnectToIntifaceOnLogin(this.clientManager), this);
    getServer().getPluginManager().registerEvents(new VibrateOnBlockBreak(this, this.clientManager), this);
    getServer().getPluginManager().registerEvents(new VibrateOnDamage(this, this.clientManager), this);

    getConfig().options().copyDefaults(true);
    saveConfig();
    getLogger().info("Buttplug MC is enabled.");
  }

  @Override
  public void onDisable() {
    this.clientManager.getClients().forEach(client -> {
      client.stopAllDevices();
      client.close();
    });
    saveConfig();
    getLogger().info("Buttplug MC is disabled.");
  }
}
