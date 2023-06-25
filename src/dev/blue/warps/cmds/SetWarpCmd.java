package dev.blue.warps.cmds;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import dev.blue.warps.Main;
import dev.blue.warps.Warp;

public class SetWarpCmd implements CommandExecutor {
	
	private Main main;
	
	public SetWarpCmd(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			sender.sendMessage(ChatColor.RED + "Only players can use these commands!");
		Player p = (Player) sender;
		if (!p.hasPermission("warp.create") && !p.isOp() && this.main.usePermissions()) {
			p.sendMessage(this.main.getMsgs().NO_PERMS_CMD());
			return true;
		}
		int created = 0;
		List<Warp> warps = this.main.getUtils().getWarps();
		for (int i = 0; i < warps.size(); i++) {
			if (p.getUniqueId().toString().equalsIgnoreCase(((Warp) warps.get(i)).getCreator()))
				created++;
		}
		int limit = 1;
		if (this.main.getConfig().contains("Warp-Limit"))
			limit = this.main.getConfig().getInt("Warp-Limit");
		if (this.main.usePermissions())
			for (PermissionAttachmentInfo each : p.getEffectivePermissions()) {
				if (each.getPermission().startsWith("warp.limit.")
						&& StringUtils.isNumeric(each.getPermission().replaceAll("warp.limit.", "")))
					limit = Integer.parseInt(each.getPermission().replaceAll("warp.limit.", ""));
			}
		if (p.hasPermission("warp.limit.*") || limit > created || p.isOp()) {
			if (args.length == 0) {
				p.sendMessage(ChatColor.RED + "Improper usage! Use /setwarp <warpName>");
			} else if (args.length == 1) {
				if (this.main.getFileBuilder().canAddWarp(p.getLocation(), args[0], p)) {
					p.sendMessage(this.main.getMsgs().WARP_CREATED(args[0]));
				} else {
					p.sendMessage(this.main.getMsgs().WARP_ALREADY_EXISTS(args[0]));
				}
			}
		} else if (limit <= created) {
			p.sendMessage(this.main.getMsgs().WARP_LIMIT_REACHED(limit));
		} else {
			p.sendMessage(this.main.getMsgs().NO_PERMS_CMD());
		}
		return true;
	}
}
