package uk.co.oliwali.HelloWorld;

import java.util.logging.Logger;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HelloWorld extends JavaPlugin {
	
	public String name;
	public String version;
	public Config config;
	private HWPlayerListener playerListener = new HWPlayerListener(this);
	public static final Logger log = Logger.getLogger("Minecraft");
	private GroupManager gm;
	
	public void onDisable() {
		sendMessage("info", "Version " + version + " disabled!");
	}
	
	public void onEnable() {

		//Set up config and database
		name = this.getDescription().getName();
        version = this.getDescription().getVersion();
        config = new Config(this);
        
        //Set up permissions
        Plugin p = this.getServer().getPluginManager().getPlugin("GroupManager");
        if (p != null) {
            if (!this.getServer().getPluginManager().isPluginEnabled(p)) {
                this.getServer().getPluginManager().enablePlugin(p);
            }
            gm = (GroupManager) p;
        } else {
            this.getPluginLoader().disablePlugin(this);
        }
        
        // Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_TELEPORT, playerListener, Event.Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Event.Priority.Monitor, this);
        sendMessage("info", "Version " + version + " enabled!");
        
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
		
		String prefix = cmd.getName();
		Player player = (Player) sender;
		
		if (prefix.equalsIgnoreCase("goto") && getPermission(player, "helloworld.goto")) {
			if (args.length == 0) {
				sendMessage(player, "Please supply a world name!");
			}
			else if (config.getWorldFromAlias(args[0]) == null) {
				sendMessage(player, "Invalid world name!");
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
	
	public boolean getPermission(Player player, String node) {
		return gm.getWorldsHolder().getWorldPermissions(player).has(player, node);
	}
	
	public void sendMessage(Player player, String msg) {
		player.sendMessage(replaceColors(msg));
	}
	
	public void sendMessage(String level, String msg) {
		msg = "[" + name + "] " + msg;
		if (level == "info")
			log.info(msg);
		else
			log.severe(msg);
	}
	
    public String replaceColors(String str) {
        str = str.replace("`c", ChatColor.RED.toString());
        str = str.replace("`4", ChatColor.DARK_RED.toString());
        str = str.replace("`e", ChatColor.YELLOW.toString());
        str = str.replace("`6", ChatColor.GOLD.toString());
        str = str.replace("`a", ChatColor.GREEN.toString());
        str = str.replace("`2", ChatColor.DARK_GREEN.toString());
        str = str.replace("`b", ChatColor.AQUA.toString());
        str = str.replace("`8", ChatColor.DARK_AQUA.toString());
        str = str.replace("`9", ChatColor.BLUE.toString());
        str = str.replace("`1", ChatColor.DARK_BLUE.toString());
        str = str.replace("`d", ChatColor.LIGHT_PURPLE.toString());
        str = str.replace("`5", ChatColor.DARK_PURPLE.toString());
        str = str.replace("`0", ChatColor.BLACK.toString());
        str = str.replace("`8", ChatColor.DARK_GRAY.toString());
        str = str.replace("`7", ChatColor.GRAY.toString());
        str = str.replace("`f", ChatColor.WHITE.toString());
        return str;
    }
	
}
