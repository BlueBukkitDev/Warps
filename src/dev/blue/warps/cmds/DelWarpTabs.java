package dev.blue.warps.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.blue.warps.Main;
import dev.blue.warps.Warp;

public class DelWarpTabs implements TabCompleter {
	
	private Main main;
	
	public DelWarpTabs(Main main) {
		this.main = main;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)){
			return null;
		}
		Player p = (Player) sender;
		List<Warp> warps = main.getUtils().getWarps(p);
		List<String> options = new ArrayList<>();
		for(Warp each:warps) {
			options.add(each.getName());
		}
		return options;
	}
}
