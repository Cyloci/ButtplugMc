package com.psiloclast.utils;

import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Plugin extends JavaPlugin {

  private final HashMap<String, CommandHandler> commands = new HashMap<>();

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return false;
    }
    Player player = (Player) sender;
    String commandName = command.getName().toLowerCase();
    CommandHandler handler = commands.get(commandName);
    if (handler == null) {
      return false;
    }
    return handler.handleCommand(player, args);
  }

  protected void registerCommandHandler(String name, CommandHandler command) {
    commands.put(name, command);
  }

  protected void registerListener(Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }
}
