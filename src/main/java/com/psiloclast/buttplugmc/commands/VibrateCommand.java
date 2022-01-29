package com.psiloclast.buttplugmc.commands;

import io.buttplug.ButtplugClient;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class VibrateCommand {

  private final HashMap<UUID, ButtplugClient> buttplugClients;

  public VibrateCommand(HashMap<UUID, ButtplugClient> buttplugClients) {
    this.buttplugClients = buttplugClients;
  }

  public void handleCommand(Player player, double level) {
    ButtplugClient client = this.buttplugClients.get(player.getUniqueId());
    if (client == null || !client.isConnected()) {
      player.sendMessage(ChatColor.RED + "You must first connect your toy with /add-toy");
      return;
    }
    if (level < 0 || level > 100) {
      player.sendMessage(ChatColor.RED + "'level' must be between 0 and 100");
      return;
    }
    client.getDevices().values().forEach(device -> {
      device.vibrate(level / 100.0);
    });
    return;
  }
}
