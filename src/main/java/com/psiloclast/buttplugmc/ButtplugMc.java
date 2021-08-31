package com.psiloclast.buttplugmc;

import com.psiloclast.buttplugmc.commands.AddToyCommand;
import com.psiloclast.buttplugmc.commands.VibrateCommand;
import com.psiloclast.buttplugmc.listeners.VibrateOnBlockBreak;
import com.psiloclast.buttplugmc.listeners.VibrateOnDamage;
import com.psiloclast.utils.Plugin;
import io.buttplug.ButtplugClient;
import java.util.HashMap;
import java.util.UUID;

public class ButtplugMc extends Plugin {

  private HashMap<UUID, ButtplugClient> buttplugClients;

  @Override
  public void onEnable() {
    this.buttplugClients = new HashMap<>();

    registerCommandHandler("add-toy", new AddToyCommand(this, buttplugClients));
    registerCommandHandler("vibrate", new VibrateCommand(this, buttplugClients));
    registerListener(new VibrateOnBlockBreak(this, buttplugClients));
    registerListener(new VibrateOnDamage(this, buttplugClients));

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
