package dev.blue.warps;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Cmds implements CommandExecutor {
	private Main main;

	public Cmds(Main main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
			sender.sendMessage(ChatColor.RED + "Only players can use these commands!");
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("setwarp")) {
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
		} else if (cmd.getName().equalsIgnoreCase("delwarp")) {
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
		} else if (cmd.getName().equalsIgnoreCase("warps")) {
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
		} else if (cmd.getName().equalsIgnoreCase("warp")) {
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
		}
		return true;
	}
}
