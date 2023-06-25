package dev.blue.warps;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Warp {
	private Location wLoc;

	private String wName;

	private String wCreator = "null";

	private Main main;

	private ItemStack guiItem;

	public Warp(String name, Main main) {
		this.main = main;
		if (name == null)
			return;
		if (!main.getFileBuilder().getWarps().contains(name))
			return;
		Material material = Material.getMaterial(main.getConfig().getString("Default-GUI-Warp-Item"));
		if (material == null)
			material = Material.OAK_SIGN;
		if (main.getWarps().getItemStack(String.valueOf(name) + ".GUI-Item") == null) {
			this.guiItem = new ItemStack(material);
		} else {
			this.guiItem = main.getWarps().getItemStack(String.valueOf(name) + ".GUI-Item");
			this.wName = name;
			this.wLoc = main.getWarps().getLocation(String.valueOf(name) + ".Location");
			if (main.getWarps().getString(String.valueOf(name) + ".Creator") == null
					|| main.getWarps().getString(String.valueOf(name) + ".Creator").length() < 1) {
				this.wCreator = "null";
			} else {
				this.wCreator = main.getWarps().getString(String.valueOf(name) + ".Creator");
			}
			return;
		}
		ItemMeta meta = this.guiItem.getItemMeta();
		meta.setDisplayName(name);
		meta.getPersistentDataContainer().set((main.getUtils()).warpKey, PersistentDataType.STRING, name);
		this.guiItem.setItemMeta(meta);
		this.wName = name;
		this.wLoc = main.getWarps().getLocation(String.valueOf(name) + ".Location");
		if (main.getWarps().getString(String.valueOf(name) + ".Creator") == null
				|| main.getWarps().getString(String.valueOf(name) + ".Creator").length() < 1) {
			this.wCreator = "null";
		} else {
			this.wCreator = main.getWarps().getString(String.valueOf(name) + ".Creator");
		}
	}

	public ItemStack getGUIItem() {
		return this.guiItem;
	}

	public void setGUIItem(ItemStack itemStack) {
		ItemStack stack = itemStack.clone();
		ItemMeta meta = stack.getItemMeta();
		meta.getPersistentDataContainer().set((this.main.getUtils()).warpKey, PersistentDataType.STRING, this.wName);
		stack.setItemMeta(meta);
		this.main.getWarps().set(String.valueOf(String.valueOf(this.wName)) + ".GUI-Item", stack);
		this.main.saveWarps();
		this.guiItem = stack;
	}

	public boolean exists() {
		if (this.wName != null)
			return true;
		return false;
	}

	public void warpPlayer(final Player p) {
		final long rand = (new Random()).nextLong();
		this.main.getUtils().preparePlayerToWarp(p, rand);
		p.sendMessage(this.main.getMsgs().WARP_COUNTDOWN(this.wName));
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				if (Warp.this.main.getUtils().isReadyToWarp(p, rand)) {
					p.teleport(Warp.this.wLoc);
					p.playSound(p.getLocation(), Sound.valueOf(Warp.this.main.getConfig().getString("Teleport-Sound")),
							1.0F, 1.0F);
					p.sendMessage(Warp.this.main.getMsgs().WARP_MESSAGE(Warp.this.wName));
					Warp.this.main.getUtils().cancelPreparePlayerForWarp(p);
				}
			}
		};
		runnable.runTaskLater((Plugin) this.main, (this.main.getConfig().getInt("Warp-Delay") * 20));
	}

	public Location getLocation() {
		return this.wLoc;
	}

	public String getName() {
		return this.wName;
	}

	public String getCreator() {
		return this.wCreator;
	}
}
