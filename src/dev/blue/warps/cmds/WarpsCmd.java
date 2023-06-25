package dev.blue.warps.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.blue.warps.GUIPage;
import dev.blue.warps.Main;

public class WarpsCmd implements CommandExecutor {
	
	private Main main;
	
	public WarpsCmd(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)){
			return true;
		}
		Player p = (Player) sender;
		if (args.length == 0) {
			if (!p.hasPermission("warp.list") && !p.isOp() && this.main.usePermissions()) {
				p.sendMessage(this.main.getMsgs().NO_PERMS_CMD());
				return true;
			}
			if (this.main.getConfig().getString("GUI-Or-List").equalsIgnoreCase("gui")) {
				(new GUIPage(0, p, this.main)).openGUI();
			} else {
				String msg = "";
				if (!this.main.getFileBuilder().getWarpsByPermission(p).isEmpty()) {
					for (String each : this.main.getFileBuilder().getWarpsByPermission(p))
						msg = String.valueOf(String.valueOf(String.valueOf(msg))) + each + ", ";
					msg.trim();
					if (msg.endsWith(","))
						msg.substring(0, msg.length() - 1);
					String prior = this.main.getConfig().getString("Warps-List").replaceAll("%warps%", msg);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', prior));
				} else {
					msg = ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("No-Warps"));
					p.sendMessage(msg);
				}
			}
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("gui") && (p.hasPermission("warp.gui") || p.isOp())
					&& this.main.usePermissions()) {
				(new GUIPage(0, p, this.main)).openGUI();
			} else {
				p.sendMessage(this.main.getMsgs().NO_PERMS_CMD());
			}
		} else {
			p.sendMessage(ChatColor.RED + "Improper usage! Use /warps");
		}
		return true;
	}
}
