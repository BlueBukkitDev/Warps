package dev.blue.warps.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.blue.warps.Main;
import dev.blue.warps.Warp;

public class WarpTabs implements TabCompleter {
	
	private Main main;
	
	public WarpTabs(Main main) {
		this.main = main;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)){
			return null;
		}
		Player p = (Player) sender;
		List<String> options = new ArrayList<>();
		if(args.length == 1) {
			for(Warp each:main.getUtils().getUsableWarps(p)) {
				options.add(each.getName());
			}
		}
		return options;
	}
}
