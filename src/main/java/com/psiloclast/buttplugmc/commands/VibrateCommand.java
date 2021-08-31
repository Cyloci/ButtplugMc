package com.psiloclast.buttplugmc.commands;

import com.psiloclast.utils.CommandHandler;

import io.buttplug.ButtplugClient;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public class VibrateCommand implements CommandHandler {

  private final JavaPlugin plugin;
  private final HashMap<UUID, ButtplugClient> buttplugClients;

  public VibrateCommand(JavaPlugin plugin, HashMap<UUID, ButtplugClient> buttplugClients) {
    this.plugin = plugin;
    this.buttplugClients = buttplugClients;
  }

  @Override
  public boolean handleCommand(Player player, String[] args) {
    ButtplugClient client = this.buttplugClients.get(player.getUniqueId());
    if (client == null) {
      player.sendMessage(ChatColor.RED + "You must first connect your toy with /add-toy");
      return true;
    }
    if (args.length != 1) {
      player.sendMessage(ChatColor.RED + "Usage: /vibrate <level>");
      return true;
    }
    final double level;
    try {
      level = Integer.parseInt(args[0]) / 100.0;
    } catch (NumberFormatException e) {
      this.plugin.getLogger().info(e.toString());
      return true;
    }
    if (level < 0 || level > 1) {
      player.sendMessage(ChatColor.RED + "'level' must be between 0 and 100");
      return true;
    }
    client.getDevices().values().forEach(device -> {
      device.vibrate(level);
    });
    return true;
  }
}
