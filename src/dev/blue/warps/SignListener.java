package dev.blue.warps;

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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class SignListener implements Listener {
	private static List<Location> selectedForDestruction = new ArrayList<>();

	private Main main;

	public SignListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (!e.getLine(0).equalsIgnoreCase("warp"))
			return;
		if (this.main.usePermissions() && !p.hasPermission("warp.sign.create.*")
				&& !p.hasPermission("warp.sign.create.own") && !p.isOp()) {
			e.setCancelled(true);
			p.sendMessage(this.main.getMsgs().SIGN_CREATE_NO_PERMISSION());
		}
		if (!p.hasPermission("warp.sign.create.*") && 
				!p.isOp() && 
				!this.main.getFileBuilder().getWarp(e.getLine(1).toLowerCase()).getCreator().equalsIgnoreCase(p.getUniqueId().toString()) &&
				this.main.usePermissions()) {
			e.setCancelled(true);
			p.sendMessage(this.main.getMsgs().SIGN_CREATE_NO_PERMISSION());
			return;
		}
		if (this.main.getFileBuilder().getWarps().contains(e.getLine(1).toLowerCase())) {
			p.sendMessage(this.main.getMsgs().SIGN_CREATED(e.getLine(1).toLowerCase()));
			Sign sign = (Sign) e.getBlock().getState();
			sign.getPersistentDataContainer().set((this.main.getUtils()).warpKey, PersistentDataType.STRING,
					e.getLine(1).toLowerCase());
			sign.update();
			e.setLine(0, ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("SignFormat.Line1"))
				.replaceAll("%warp%", e.getLine(1)).replaceAll("%line1%", e.getLine(0))
				.replaceAll("%line2%", e.getLine(1)).replaceAll("%line3%", e.getLine(2))
				.replaceAll("%line4%", e.getLine(3)));
			e.setLine(1, ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("SignFormat.Line2"))
				.replaceAll("%warp%", e.getLine(1)).replaceAll("%line1%", e.getLine(0))
				.replaceAll("%line2%", e.getLine(1)).replaceAll("%line3%", e.getLine(2))
				.replaceAll("%line4%", e.getLine(3)));
			e.setLine(2, ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("SignFormat.Line3"))
				.replaceAll("%warp%", e.getLine(1)).replaceAll("%line1%", e.getLine(0))
				.replaceAll("%line2%", e.getLine(1)).replaceAll("%line3%", e.getLine(2))
				.replaceAll("%line4%", e.getLine(3)));
			e.setLine(3, ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("SignFormat.Line4"))
				.replaceAll("%warp%", e.getLine(1)).replaceAll("%line1%", e.getLine(0))
				.replaceAll("%line2%", e.getLine(1)).replaceAll("%line3%", e.getLine(2))
				.replaceAll("%line4%", e.getLine(3)));
		} else {
			e.setCancelled(true);
			p.sendMessage(this.main.getMsgs().WARP_DOES_NOT_EXIST(e.getLine(1).toLowerCase()));
			return;
		}
	}

	@EventHandler
	public void onWarp(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (e.getHand() != EquipmentSlot.HAND)
			return;
		Block b = e.getClickedBlock();
		if (!(b.getState() instanceof Sign))
			return;
		Sign sign = (Sign) b.getState();
		if (sign.getPersistentDataContainer().has((this.main.getUtils()).warpKey, PersistentDataType.STRING)) {
			String string = ((String) sign.getPersistentDataContainer().get((this.main.getUtils()).warpKey,
					PersistentDataType.STRING)).toLowerCase();
			Warp w = new Warp(string, this.main);
			if (!w.exists()) {
				p.sendMessage(this.main.getMsgs().WARP_DOES_NOT_EXIST(string));
			} else if (p.hasPermission("warp.use.*") || p.hasPermission("warp.use." + string) || p.isOp()
					|| !this.main.usePermissions()) {
				w.warpPlayer(p);
			} else {
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this warp!");
			}
		}
	}

	@EventHandler
	public void onSignBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		final Block block = e.getBlock();
		BlockState state = block.getState();
		if (!(state instanceof Sign))
			return;
		Sign sign = (Sign) state;
		if (!sign.getPersistentDataContainer().has((this.main.getUtils()).warpKey, PersistentDataType.STRING))
			return;
		if (!p.hasPermission("warp.sign.destroy.*") && !p.isOp() && p.hasPermission("warp.sign.destroy.own"))
			if (!(new Warp((String) sign.getPersistentDataContainer().get((this.main.getUtils()).warpKey,
					PersistentDataType.STRING), this.main)).getCreator().equalsIgnoreCase(p.getUniqueId().toString())
					&& this.main.usePermissions()) {
				e.setCancelled(true);
				return;
			}
		String string = ((String) sign.getPersistentDataContainer().get((this.main.getUtils()).warpKey,
				PersistentDataType.STRING)).toLowerCase();
		if (!selectedForDestruction.contains(block.getLocation())) {
			selectedForDestruction.add(block.getLocation());
			e.setCancelled(true);
			p.sendMessage(this.main.getMsgs().SIGN_BROKEN_CONFIRM(string));
			this.main.scheduler.scheduleSyncDelayedTask((Plugin) this.main, new Runnable() {
				public void run() {
					SignListener.selectedForDestruction.remove(block.getLocation());
				}
			}, 20L);
		} else {
			p.sendMessage(this.main.getMsgs().SIGN_BROKEN(string));
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (!this.main.getConfig().getBoolean("Cancel-Warp-On-Movement"))
			return;
		if (e.getFrom().getBlock().getLocation().distance(e.getTo().getBlock().getLocation()) < 0.2D)
			return;
		if (this.main.getUtils().cancelPreparePlayerForWarp(e.getPlayer()))
			e.getPlayer().sendMessage(this.main.getMsgs().WARP_CANCELLED());
	}
}
