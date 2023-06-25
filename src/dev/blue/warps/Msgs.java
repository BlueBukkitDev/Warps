package dev.blue.warps;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Msgs {
	private Main main;

	public Msgs(Main main) {
		this.main = main;
	}

	public String PREFIX() {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Prefix"));
	}

	public String WARP_ALREADY_EXISTS(String warp) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Already-Exists")
				.replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
	}

	public String WARP_DOES_NOT_EXIST(String warp) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Invalid-Warp-Name")
				.replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
	}

	public String WARPS_LIST(Player p) {
		if (!this.main.getFileBuilder().getWarpsByPermission(p).isEmpty()) {
			String msg = "";
			for (String each : this.main.getFileBuilder().getWarpsByPermission(p))
				msg = String.valueOf(String.valueOf(String.valueOf(msg))) + each + ", ";
			msg.trim();
			if (msg.endsWith(","))
				msg.substring(0, msg.length());
			return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warps-List")
					.replaceAll("%warps%", msg).replaceAll("%prefix%", PREFIX()));
		}
		return ChatColor.translateAlternateColorCodes('&',
				this.main.getConfig().getString("No-Warps").replaceAll("%prefix%", PREFIX()));
	}

	public String WARP_MESSAGE(String warp) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Message")
				.replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
	}

	public String WARP_CREATED(String warp) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Set")
				.replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
	}

	public String WARP_DELETED(String warp) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Deleted")
				.replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
	}

	public String SIGN_CREATED(String warp) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Sign-Created")
				.replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
	}

	public String SIGN_BROKEN(String warp) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Sign-Broken")
				.replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
	}

	public String SIGN_BROKEN_CONFIRM(String warp) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Sign-Broken-Confirm")
				.replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
	}

	public String SIGN_CREATE_NO_PERMISSION() {
		return ChatColor.translateAlternateColorCodes('&',
				this.main.getConfig().getString("Sign-Create-No-Permission").replaceAll("%prefix%", PREFIX()));
	}

	public String SIGN_DESTROY_NO_PERMISSION() {
		return ChatColor.translateAlternateColorCodes('&',
				this.main.getConfig().getString("Sign-Destroy-No-Permission").replaceAll("%prefix%", PREFIX()));
	}

	public String WARP_CANCELLED() {
		return ChatColor.translateAlternateColorCodes('&',
				this.main.getConfig().getString("Warp-Cancelled").replaceAll("%prefix%", PREFIX()));
	}

	public String WARP_COUNTDOWN(String warp) {
		return

		ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Countdown"))
				.replaceAll("%prefix%", PREFIX()).replaceAll("%warp%", warp)
				.replaceAll("%time%", ""+this.main.getConfig().getInt("Warp-Delay"));
	}

	public String NO_PERMS_CMD() {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("No-Permission-For-Command"))
				.replaceAll("%prefix%", PREFIX());
	}

	public String WARP_ITEM_SET(String warp, String item) {
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Item-Set"))
				.replaceAll("%prefix%", PREFIX()).replaceAll("%warp%", warp).replaceAll("%item%", item);
	}

	public String WARP_LIMIT_REACHED(int limit) {
		if (!this.main.getConfig().contains("Warp-Limit-Reached")) {
			this.main.getConfig().set("Warp-Limit-Reached",
					"You have created as many warps as you are allowed! (%limit%)");
			this.main.saveConfig();
		}
		return ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("Warp-Limit-Reached"))
				.replaceAll("%limit%", ""+limit).replaceAll("%prefix%", PREFIX());
	}
}
