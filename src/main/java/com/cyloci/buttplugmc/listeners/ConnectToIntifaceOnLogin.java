package com.cyloci.buttplugmc.listeners;

import com.cyloci.buttplugmc.ButtplugClientManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectToIntifaceOnLogin implements Listener {

    private final ButtplugClientManager clientManager;

    public ConnectToIntifaceOnLogin(ButtplugClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.clientManager.getClient(player);
    }
}
