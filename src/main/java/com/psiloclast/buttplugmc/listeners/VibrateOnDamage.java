package com.psiloclast.buttplugmc.listeners;

import java.util.HashMap;
import java.util.UUID;

import com.psiloclast.utils.Sleep;

import io.buttplug.ButtplugClient;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VibrateOnDamage implements Listener {

  private final String DAMAGE_VIBRATE_OPTIONS = "damage-vibrate-options";
  private final JavaPlugin plugin;
  private final HashMap<UUID, ButtplugClient> buttplugClients;

  public VibrateOnDamage(JavaPlugin plugin, HashMap<UUID, ButtplugClient> buttplugClients) {
    this.plugin = plugin;
    this.buttplugClients = buttplugClients;
    this.plugin.getConfig().addDefault(DAMAGE_VIBRATE_OPTIONS + ".level", 50);
    this.plugin.getConfig().addDefault(DAMAGE_VIBRATE_OPTIONS + ".duration", 250);
  }

  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
      return;
    }
    Player player = (Player) event.getEntity();
    ButtplugClient client = this.buttplugClients.get(player.getUniqueId());
    if (client == null) {
      return;
    }
    int level = this.plugin.getConfig().getInt(DAMAGE_VIBRATE_OPTIONS + ".level");
    int duration = this.plugin.getConfig().getInt(DAMAGE_VIBRATE_OPTIONS + ".duration");
    client.getDevices().values().forEach(device -> {
      device.vibrate(level / 100.0);
      Sleep.sleep(duration);
      device.vibrate(0.0);
    });
  }
}
