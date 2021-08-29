package com.psiloclast.buttplugmc.commands;

import com.psiloclast.utils.CommandHandler;
import io.buttplug.ButtplugDevice;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class VibrateCommand implements CommandHandler {

  private final JavaPlugin plugin;
  private final ButtplugDevice device;

  public VibrateCommand(JavaPlugin plugin, ButtplugDevice device) {
    this.plugin = plugin;
    this.device = device;
  }

  @Override
  public boolean handleCommand(Player player, String[] args) {
    if (args.length != 1) {
      player.sendMessage(ChatColor.RED + "Usage: /vibrate <level>");
      return true;
    }
    double level = 0;
    try {
      level = Integer.parseInt(args[0]) / 100.0;
    } catch (Exception e) {
      this.plugin.getLogger().info(e.toString());
      return true;
    }
    if (level < 0 || level > 1) {
      player.sendMessage(ChatColor.RED + "'level' must be between 0 and 100");
      return true;
    }
    this.device.vibrate(level);
    return true;
  }
}
