package dev.blue.warps.cmds;

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
			if (!p.hasPermission("warp.gui") && !p.isOp() && this.main.usePermissions()) {
				p.sendMessage(this.main.getMsgs().NO_PERMS_CMD());
				return true;
			}
			new GUIPage(0, p, this.main).openGUI();
		}
		return true;
	}
}
