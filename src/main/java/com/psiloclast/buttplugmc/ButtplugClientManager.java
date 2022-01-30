package com.psiloclast.buttplugmc;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import com.psiloclast.utils.Sleep;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.buttplug.ButtplugClient;
import io.buttplug.WebsocketConnectorOptions;
import net.md_5.bungee.api.ChatColor;

public class ButtplugClientManager {

    private final JavaPlugin plugin;
    private final HashMap<UUID, ButtplugClient> clients;

    public ButtplugClientManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.clients = new HashMap<>();
    }

    public Collection<ButtplugClient> getClients() {
        return this.clients.values();
    }

    public ButtplugClient getClient(Player player) {
        UUID playerId = player.getUniqueId();
        boolean withPlayerFeedback = false;
        ButtplugClient client = this.clients.get(playerId);
        if (client == null || !client.isConnected()) {
            player.sendMessage(ChatColor.AQUA + "Reconnecting to Intiface...");
            withPlayerFeedback = true;
            client = connectButtplugClient(player.getAddress().getAddress());
            clients.put(playerId, client);
        }
        scanForToys(client, player, withPlayerFeedback);
        return client;
    }

    private ButtplugClient connectButtplugClient(InetAddress address) {
        URI uri;
        try {
            uri = new URI("ws://" + address.getHostAddress() + ":12345/buttplug");
        } catch (URISyntaxException e) {
            this.plugin.getLogger().info(e.toString());
            return null;
        }
        ButtplugClient client = new ButtplugClient("ButtplugMC");
        WebsocketConnectorOptions connector = new WebsocketConnectorOptions(uri);
        client.connect(connector);
        return client;
    }

    private void scanForToys(ButtplugClient client, Player player, boolean withPlayerFeedback) {
        int attempts = 0;
        while (client.getDevices().size() == 0) {
            client.startScanning();
            if (withPlayerFeedback) {
                player.sendMessage(ChatColor.AQUA + "Searching for toys...");
            }
            Sleep.sleep(1000);
            attempts++;
            if (attempts == 5) {
                if (withPlayerFeedback) {
                    player.sendMessage(ChatColor.RED + "Couldn't find any toys.");
                }
                return;
            }
        }
        if (withPlayerFeedback) {
            if (client.getDevices().size() > 0) {
                player.sendMessage(ChatColor.AQUA + "Connected to the following toys:");
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
            } else {
                player.sendMessage(ChatColor.RED + "Could not find any toys.");
            }
        }
    }

}
