package com.psiloclast.buttplugmc;

import com.psiloclast.buttplugmc.commands.VibrateCommand;
import com.psiloclast.buttplugmc.listeners.VibrateOnBlockBreak;
import com.psiloclast.buttplugmc.listeners.VibrateOnDamage;
import com.psiloclast.utils.Plugin;
import com.psiloclast.utils.Sleep;
import io.buttplug.ButtplugClient;
import io.buttplug.ButtplugDevice;
import io.buttplug.WebsocketConnectorOptions;
import java.net.URI;

public class ButtplugMc extends Plugin {

  private final URI ADDRESS = URI.create("ws://localhost:12345/buttplug");
  ButtplugClient buttplugClient;

  @Override
  public void onEnable() {
    this.buttplugClient = connectButtplug();
    ButtplugDevice device = getDevice(this.buttplugClient, 0);

    registerCommandHandler("vibrate", new VibrateCommand(this, device));
    registerListener(new VibrateOnBlockBreak(this, device));
    registerListener(new VibrateOnDamage(this, device));

    getConfig().options().copyDefaults(true);
    saveConfig();
    getLogger().info("Buttplug MC is enabled.");
  }

  @Override
  public void onDisable() {
    buttplugClient.stopAllDevices();
    buttplugClient.close();
    saveConfig();
    getLogger().info("Buttplug MC is disabled.");
  }

  private ButtplugClient connectButtplug() {
    ButtplugClient client = new ButtplugClient("buttplug-mc");
    WebsocketConnectorOptions connector = new WebsocketConnectorOptions(ADDRESS);
    client.connect(connector);
    return client;
  }

  private ButtplugDevice getDevice(ButtplugClient client, int index) {
    ButtplugDevice device = null;
    client.startScanning();
    while (device == null) {
      getLogger().info("searching for devices...");
      Sleep.sleep(1000);
      client.getDevices().forEach((key, value) -> getLogger().info(key + ":" + value));
      device = client.getDevices().get(index);
    }
    getLogger().info("found device: " + device.name);
    client.stopScanning();
    device.vibrate(1.0);
    Sleep.sleep(250);
    device.vibrate(0.0);
    Sleep.sleep(250);
    device.vibrate(1.0);
    Sleep.sleep(250);
    device.vibrate(0.0);
    return device;
  }
}
