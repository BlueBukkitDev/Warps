package dev.blue.warps.cmds;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class SetWarpTabs implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)){
			return null;
		}
		Player p = (Player) sender;
		if(args.length == 1) {
			return Arrays.asList("<name>", p.getName(), p.getLocation().getBlock().getBiome().toString().toLowerCase().replaceAll("_", "-"));
		}
		return Arrays.asList("Too many arguments!");
	}
}
