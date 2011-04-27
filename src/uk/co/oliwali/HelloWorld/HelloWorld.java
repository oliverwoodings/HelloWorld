package uk.co.oliwali.HelloWorld;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HelloWorld extends JavaPlugin {
	
	public String name;
	public String version;
	public Config config;
	private HWPlayerListener playerListener = new HWPlayerListener(this);
	public static final Logger log = Logger.getLogger("Minecraft");
	private Permission permissions;
	
	public void onDisable() {
		Util.info("Version " + version + " disabled!");
	}
	
	public void onEnable() {

		//Set up config and database
		name = this.getDescription().getName();
        version = this.getDescription().getVersion();
        config = new Config(this);
        permissions = new Permission(this);  
        
        // Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_TELEPORT, playerListener, Event.Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Event.Priority.Monitor, this);
        Util.info("Version " + version + " enabled!");
        
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
		
		String prefix = cmd.getName();
		Player player = (Player) sender;
		
		if (prefix.equalsIgnoreCase("goto") && permissions.go(player)) {
			if (args.length == 0) {
				Util.sendMessage(player, "&cPlease supply a world name!");
			}
			else if (config.getWorldFromAlias(args[0]) == null) {
				Util.sendMessage(player, "&cInvalid world name!");
			}
			else {
				String worldName = config.getWorldFromAlias(args[0]);
				World world = getServer().getWorld(worldName);
				player.teleport(world.getSpawnLocation());
			}
			return true;
		}
		return false;
		
	}
	
}
