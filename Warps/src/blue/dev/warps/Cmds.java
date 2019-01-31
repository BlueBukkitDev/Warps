package blue.dev.warps;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmds implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		FileBuilder fb = new FileBuilder();
		Msgs msgs = new Msgs();
		if ((sender instanceof Player)) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("setwarp")) {
				if ((p.hasPermission("warp.create")) || (p.isOp())) {
					if (args.length == 0) {
						p.sendMessage(ChatColor.RED + "Improper usage! Use /setwarp <warpName>");
					} else if (args.length == 1) {
						if (fb.canAddWarp(p.getLocation(), args[0])) {
							p.sendMessage(msgs.WARP_CREATED(args[0]));
						} else {
							p.sendMessage(msgs.WARP_ALREADY_EXISTS(args[0]));
						}
					}
				}
			} else if (cmd.getName().equalsIgnoreCase("delwarp")) {
				if ((p.hasPermission("warp.delete")) || (p.isOp())) {
					if (args.length == 0) {
						p.sendMessage(ChatColor.RED + "Improper usage! Use /delwarp <warpName>");
					} else if (args.length == 1) {
						if (fb.canDeleteWarp(args[0])) {
							p.sendMessage(msgs.WARP_DELETED(args[0]));
						} else {
							p.sendMessage(msgs.WARP_DOES_NOT_EXIST(args[0]));
						}
					}
				}
			} else if (cmd.getName().equalsIgnoreCase("warps")) {
				if (args.length == 0) {
					if ((p.hasPermission("warp.list")) || (p.isOp())) {
						if (Main.plugin.getConfig().getString("GUI-Or-List").equalsIgnoreCase("gui")) {
							new GUIPage(0, p).openGUI();
						} else {
							String msg = "";
							if (!FileBuilder.getWarps(p).isEmpty()) {
								for (String each : FileBuilder.getWarps(p)) {
									msg = msg + each + ", ";
								}
								msg.trim();
								if (msg.endsWith(",")) {
									msg.substring(0, msg.length() - 1);
								}
								String prior = Main.plugin.getConfig().getString("Warps-List").replaceAll("%warps%",
										msg);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', prior));
							} else {
								msg = ChatColor.translateAlternateColorCodes('&',
										Main.plugin.getConfig().getString("No-Warps"));
								p.sendMessage(msg);
							}
						}
					}
				} else if (args.length == 1) {
					if ((args[0].equalsIgnoreCase("gui")) && ((p.hasPermission("warp.gui")) || (p.isOp()))) {
						new GUIPage(0, p).openGUI();
					}
				} else {
					p.sendMessage(ChatColor.RED + "Improper usage! Use /warps");
				}
			} else if (cmd.getName().equalsIgnoreCase("warp")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED + "Improper usage! Use /warp <warpName>");
				} else if (args.length == 1) {
					if ((p.hasPermission("warp.use.*")) || (p.hasPermission("warp.use." + args[0].toLowerCase()))
							|| (p.isOp())) {
						args[0] = args[0].toLowerCase();
						Warp w = new Warp(args[0]);
						if (!w.exists()) {
							p.sendMessage(msgs.WARP_DOES_NOT_EXIST(args[0]));
						} else {
							w.warpPlayer(p);
						}
					}
				} else if ((args.length == 2) && (args[1].equalsIgnoreCase("setitem"))
						&& ((p.hasPermission("warp.item")) || (p.isOp()))) {
					if (p.getInventory().getItemInMainHand() != null) {
						fb.setItemForWarp(args[0].toLowerCase(), p.getInventory().getItemInMainHand());
						p.sendMessage(ChatColor.GOLD + "You have set warp " + args[0] + "'s GUI item.");
					} else {
						p.sendMessage(ChatColor.RED + "You must be holding an item in your hand to do this!");
					}
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players can use these commands!");
		}
		return true;
	}
}
