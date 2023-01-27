package com.cyloci.buttplugmc.commands;

import io.buttplug.ButtplugClient;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.cyloci.buttplugmc.ButtplugClientManager;

public class VibrateCommand {

  private final ButtplugClientManager clientManager;

  public VibrateCommand(ButtplugClientManager clientManager) {
    this.clientManager = clientManager;
  }

  public void handleCommand(Player player, double level) {
    if (level < 0 || level > 100) {
      player.sendMessage(ChatColor.RED + "'level' must be between 0 and 100");
      return;
    }
    ButtplugClient client = this.clientManager.getClient(player);
    client.getDevices().values().forEach(device -> {
      device.vibrate(level / 100.0);
    });
    return;
  }
}
