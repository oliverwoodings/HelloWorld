package uk.co.oliwali.HelloWorld;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;

public class Config {
	
	HashMap<String, String> messages = new HashMap<String, String>();
	HashMap<String, String> aliases = new HashMap<String, String>();
	HelloWorld plugin;
	Configuration config;

	public Config (HelloWorld instance) {
		
		this.plugin = instance;
		this.config = plugin.getConfiguration();
		config.load();
		
		//If there are no messages yet
		if (config.getKeys("worlds") == null) {
			World[] worlds = (World[]) plugin.getServer().getWorlds().toArray(new World[0]);
			for (World world : worlds) {
				config.setProperty("worlds." + world.getName() + ".message", "Welcome to " + world.getName() + "!");
				config.setProperty("worlds." + world.getName() + ".alias", "");
			}
		}
		
		//Load messages into hashmaps
		String[] worlds = (String[]) config.getKeys("worlds").toArray(new String[0]);
		for (String world : worlds) {
			messages.put(world, config.getString("worlds." + world + ".message"));
			aliases.put(config.getString("worlds." + world + ".alias"), world);
		}
		
		//Attempt save
		if (!config.save())
			plugin.sendMessage("severe", "Error while writing to config.yml");

	}
	
	public String getWorldMessage(World world) {
		return messages.get(world.getName());
	}
	
	public String getWorldFromAlias(String alias) {
		return aliases.get(alias);
	}
	
}