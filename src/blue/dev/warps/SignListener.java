package blue.dev.warps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
	private static List<Location> selectedForDestruction = new ArrayList<Location>();

	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		Player p = e.getPlayer();
		FileBuilder fb = new FileBuilder();
		if (e.getLine(0).equalsIgnoreCase("warp")) {
			if (p.hasPermission("warp.sign.create")) {
				if (FileBuilder.getWarps().contains(e.getLine(1).toLowerCase())) {
					p.sendMessage(new Msgs().SIGN_CREATED(e.getLine(1).toLowerCase()));
					e.setLine(0, ChatColor
							.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("SignFormat.Line1"))
							.replaceAll("%warp%", e.getLine(1)).replaceAll("%line1%", e.getLine(0))
							.replaceAll("%line2%", e.getLine(1)).replaceAll("%line3%", e.getLine(2))
							.replaceAll("%line4%", e.getLine(3)));
					e.setLine(1, ChatColor
							.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("SignFormat.Line2"))
							.replaceAll("%warp%", e.getLine(1)).replaceAll("%line1%", e.getLine(0))
							.replaceAll("%line2%", e.getLine(1)).replaceAll("%line3%", e.getLine(2))
							.replaceAll("%line4%", e.getLine(3)));
					e.setLine(2, ChatColor
							.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("SignFormat.Line3"))
							.replaceAll("%warp%", e.getLine(1)).replaceAll("%line1%", e.getLine(0))
							.replaceAll("%line2%", e.getLine(1)).replaceAll("%line3%", e.getLine(2))
							.replaceAll("%line4%", e.getLine(3)));
					e.setLine(3, ChatColor
							.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("SignFormat.Line4"))
							.replaceAll("%warp%", e.getLine(1)).replaceAll("%line1%", e.getLine(0))
							.replaceAll("%line2%", e.getLine(1)).replaceAll("%line3%", e.getLine(2))
							.replaceAll("%line4%", e.getLine(3)));
					fb.addWarpSign(e.getBlock().getLocation(), ChatColor.stripColor(e.getLine(1).toLowerCase()));
				} else {
					e.setCancelled(true);
					p.sendMessage(new Msgs().WARP_DOES_NOT_EXIST(e.getLine(1).toLowerCase()));
				}
			} else {
				e.setCancelled(true);
				p.sendMessage(new Msgs().SIGN_CREATE_NO_PERMISSION());
			}
		}
	}

	@EventHandler
	public void onWarp(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			if ((b.getState() instanceof Sign)) {
				Sign sign = (Sign) b.getState();
				if (sign.getLine(0).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',
						Main.plugin.getConfig().getString("SignFormat.Line1")))) {
					String w = new FileBuilder().getWarpNameBySignLocation(b.getLocation());
					w = w.toLowerCase();
					Warp wa = new Warp(w);
					if (!wa.exists()) {
						p.sendMessage(new Msgs().WARP_DOES_NOT_EXIST(w));
					} else if ((p.hasPermission("warp.use.*")) || (p.hasPermission("warp.use." + w)) || (p.isOp())) {
						wa.warpPlayer(p);
					} else {
						p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this warp!");
					}
				}
			}
		}
	}

	@EventHandler
	public void onSignBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		final Block block = e.getBlock();
		BlockState state = block.getState();
		if ((state instanceof Sign)) {
			Sign sign = (Sign) state;
			if (sign.getLine(0).contains("§1§lWARP")) {
				if ((!p.hasPermission("warp.sign.destroy")) && (!p.isOp())) {
					e.setCancelled(true);
					p.sendMessage(new Msgs().SIGN_DESTROY_NO_PERMISSION());
				} else if (!selectedForDestruction.contains(block.getLocation())) {
					selectedForDestruction.add(block.getLocation());
					e.setCancelled(true);
					Main.plugin.scheduler.scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						public void run() {
							SignListener.selectedForDestruction.remove(block.getLocation());
						}
					}, 20L);
				} else {
					String w = new FileBuilder().getWarpNameBySignLocation(block.getLocation());
					w = w.toLowerCase();
					new FileBuilder().removeWarpSign(block.getLocation());
					p.sendMessage(new Msgs().SIGN_BROKEN(w));
				}
			}
		}
	}
}
