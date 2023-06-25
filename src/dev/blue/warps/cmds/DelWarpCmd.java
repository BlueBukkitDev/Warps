package dev.blue.warps.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.blue.warps.Main;
import dev.blue.warps.Warp;

public class DelWarpCmd implements CommandExecutor {
	
	private Main main;
	
	public DelWarpCmd(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			sender.sendMessage(ChatColor.RED + "Only players can use these commands!");
		Player p = (Player) sender;
		if (!p.hasPermission("warp.delete") && !p.isOp() && !(new Warp(args[0].toLowerCase(), this.main))
				.getCreator().equalsIgnoreCase(p.getUniqueId().toString()) && this.main.usePermissions()) {
			p.sendMessage(this.main.getMsgs().NO_PERMS_CMD());
			return true;
		}
		if (args.length == 0) {
			p.sendMessage(ChatColor.RED + "Improper usage! Use /delwarp <warpName>");
		} else if (args.length == 1 && this.main.getWarps().getKeys(false).contains(args[0].toLowerCase())) {
			if (this.main.getFileBuilder().canDeleteWarp(args[0])) {
				p.sendMessage(this.main.getMsgs().WARP_DELETED(args[0]));
			} else {
				p.sendMessage(this.main.getMsgs().WARP_DOES_NOT_EXIST(args[0]));
			}
		}
		return true;
	}
}
