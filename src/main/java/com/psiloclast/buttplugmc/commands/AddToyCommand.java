package com.psiloclast.buttplugmc.commands;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

import com.psiloclast.utils.CommandHandler;
import com.psiloclast.utils.Sleep;

import io.buttplug.ButtplugClient;
import io.buttplug.WebsocketConnectorOptions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AddToyCommand implements CommandHandler {

  private final JavaPlugin plugin;
  private final HashMap<UUID, ButtplugClient> buttplugClients;

  public AddToyCommand(JavaPlugin plugin, HashMap<UUID, ButtplugClient> buttplugClients) {
    this.plugin = plugin;
    this.buttplugClients = buttplugClients;
  }

  @Override
  public boolean handleCommand(Player player, String[] args) {
    if (args.length != 1) {
      player.sendMessage(ChatColor.RED + "Usage: /add-toy <ip>");
      return true;
    }
    URI address;
    try {
      address = new URI("ws://" + args[0] + ":12345/buttplug");
    } catch (URISyntaxException e) {
      this.plugin.getLogger().info(e.toString());
      return true;
    }
    ButtplugClient client = connectButtplug(address);
    client.startScanning();
    int attempts = 0;
    while (client.getDevices().size() == 0) {
      player.sendMessage(ChatColor.AQUA + "Searching for toys...");
      Sleep.sleep(1000);
      attempts++;
      if (attempts == 3) {
        player.sendMessage(ChatColor.RED + "Couldn't find any toys.");
        return true;
      }
    }
    client.stopScanning();
    buttplugClients.put(player.getUniqueId(), client);
    player.sendMessage(ChatColor.AQUA + "Found the following toys!");
    client.getDevices().values().forEach(device -> {
      player.sendMessage(ChatColor.AQUA + device.name);
      device.vibrate(1.0);
      Sleep.sleep(250);
      device.vibrate(0.0);
      Sleep.sleep(250);
      device.vibrate(1.0);
      Sleep.sleep(250);
      device.vibrate(0.0);
    });
    return true;
  }

  private ButtplugClient connectButtplug(URI address) {
    ButtplugClient client = new ButtplugClient("buttplug-mc");
    WebsocketConnectorOptions connector = new WebsocketConnectorOptions(address);
    client.connect(connector);
    return client;
  }
}
