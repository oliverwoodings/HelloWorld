package uk.co.oliwali.HelloWorld;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class HWPlayerListener extends PlayerListener {
	
	HelloWorld plugin;
	
	public HWPlayerListener(HelloWorld instance) {
		this.plugin = instance;
	}

	public void onPlayerTeleport(PlayerTeleportEvent event) {
		
		Location from = event.getFrom();
		Location to = event.getTo();
		String message = plugin.config.getWorldMessage(to.getWorld());
		if (from.getWorld() != to.getWorld() && message != null)
			for (String msg : message.split("`n", -1))
				plugin.sendMessage(event.getPlayer(), msg);
		
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String message = plugin.config.getWorldMessage(player.getWorld());
		if (message != null)
			for (String msg : message.split("`n", -1))
				plugin.sendMessage(player, msg);
	}

}
