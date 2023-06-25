package dev.blue.warps.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.blue.warps.Main;
import dev.blue.warps.Warp;

public class WarpCmd implements CommandExecutor {
	
	private Main main;
	
	public WarpCmd(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)){
			return true;
		}
		Player p = (Player) sender;
		if (!p.hasPermission("warp.use.*") && !p.hasPermission("warp.use." + args[0].toLowerCase()) && !p.isOp()
				&& !(new Warp(args[0].toLowerCase(), this.main)).getCreator()
						.equalsIgnoreCase(p.getUniqueId().toString())
				&& this.main.usePermissions()) {
			p.sendMessage(this.main.getMsgs().NO_PERMS_CMD());
			return true;
		}
		if (args.length == 0) {
			p.sendMessage(ChatColor.RED + "Improper usage! Use /warp <warpName>");
		} else if (args.length == 1) {
			args[0] = args[0].toLowerCase();
			Warp w = new Warp(args[0], this.main);
			if (!w.exists()) {
				p.sendMessage(this.main.getMsgs().WARP_DOES_NOT_EXIST(args[0]));
			} else {
				w.warpPlayer(p);
			}
		} else if (args.length == 2 && args[1].equalsIgnoreCase("setitem")) {
			if (!p.hasPermission("warp.item.*") && !p.isOp() && !p.hasPermission("warp.item.own")
					&& this.main.usePermissions()) {
				p.sendMessage(this.main.getMsgs().NO_PERMS_CMD());
				return true;
			}
			if (p.getInventory().getItemInMainHand() != null) {
				(new Warp(args[0].toLowerCase(), this.main)).setGUIItem(p.getInventory().getItemInMainHand());
				p.sendMessage(this.main.getMsgs().WARP_ITEM_SET(args[0].toLowerCase(), this.main.getUtils()
						.formatItemName(p.getInventory().getItemInMainHand().getType().toString())));
			} else {
				p.sendMessage(ChatColor.RED + "You must be holding an item in your hand to do this!");
			}
		}
		return false;
	}
}
