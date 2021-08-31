package com.psiloclast.buttplugmc.listeners;

import com.psiloclast.utils.Sleep;

import io.buttplug.ButtplugClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public class VibrateOnBlockBreak implements Listener {

  private final String BLOCK_TO_VIBRATE_OPTIONS = "block-vibrate-options";
  private final JavaPlugin plugin;
  private final HashMap<UUID, ButtplugClient> buttplugClients;

  public VibrateOnBlockBreak(JavaPlugin plugin, HashMap<UUID, ButtplugClient> buttplugClients) {
    this.plugin = plugin;
    this.buttplugClients = buttplugClients;
    this.plugin.getConfig().addDefault(BLOCK_TO_VIBRATE_OPTIONS + ".GRASS_BLOCK.level", 10);
    this.plugin.getConfig().addDefault(BLOCK_TO_VIBRATE_OPTIONS + ".GRASS_BLOCK.duration", 100);
    this.plugin.getConfig().addDefault(BLOCK_TO_VIBRATE_OPTIONS + ".DIRT.level", 10);
    this.plugin.getConfig().addDefault(BLOCK_TO_VIBRATE_OPTIONS + ".DIRT.duration", 100);
    this.plugin.getConfig().addDefault(BLOCK_TO_VIBRATE_OPTIONS + ".STONE.level", 20);
    this.plugin.getConfig().addDefault(BLOCK_TO_VIBRATE_OPTIONS + ".STONE.duration", 150);
    this.plugin.getConfig().addDefault(BLOCK_TO_VIBRATE_OPTIONS + ".COBBLESTONE.level", 20);
    this.plugin.getConfig().addDefault(BLOCK_TO_VIBRATE_OPTIONS + ".COBBLESTONE.duration", 150);
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    ButtplugClient client = this.buttplugClients.get(event.getPlayer().getUniqueId());
    if (client == null) {
      return;
    }
    String blockName = event.getBlock().getType().name();
    int level = this.plugin.getConfig().getInt(BLOCK_TO_VIBRATE_OPTIONS + "." + blockName + ".level");
    int duration = this.plugin.getConfig().getInt(BLOCK_TO_VIBRATE_OPTIONS + "." + blockName + ".duration");
    client.getDevices().values().forEach(device -> {
      device.vibrate(level / 100.0);
      Sleep.sleep(duration);
      device.vibrate(0.0);
    });
  }
}
