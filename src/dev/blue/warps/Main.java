package dev.blue.warps;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import dev.blue.warps.cmds.DelWarpCmd;
import dev.blue.warps.cmds.DelWarpTabs;
import dev.blue.warps.cmds.SetWarpCmd;
import dev.blue.warps.cmds.SetWarpTabs;
import dev.blue.warps.cmds.WarpCmd;
import dev.blue.warps.cmds.WarpTabs;
import dev.blue.warps.cmds.WarpsCmd;
import dev.blue.warps.cmds.WarpsTabs;

public class Main extends JavaPlugin {
	private File w;

	private YamlConfiguration warps;

	public BukkitScheduler scheduler;

	private Utils utils;

	private Msgs msgs;

	private FileBuilder fb;

	private boolean usePermissions;

	public void onEnable() {
		this.scheduler = getServer().getScheduler();
		this.fb = new FileBuilder(this);
		setupYamls();
		this.utils = new Utils(this);
		this.msgs = new Msgs(this);
		getCommand("delwarp").setExecutor(new DelWarpCmd(this));
		getCommand("delwarp").setTabCompleter(new DelWarpTabs(this));
		getCommand("setwarp").setExecutor(new SetWarpCmd(this));
		getCommand("setwarp").setTabCompleter(new SetWarpTabs());
		getCommand("warp").setExecutor(new WarpCmd(this));
		getCommand("warp").setTabCompleter(new WarpTabs(this));
		getCommand("warps").setExecutor(new WarpsCmd(this));
		getCommand("warps").setTabCompleter(new WarpsTabs());
		Bukkit.getPluginManager().registerEvents(new SignListener(this), (Plugin) this);
		Bukkit.getPluginManager().registerEvents(new GUIListener(this), (Plugin) this);
		saveDefaultConfig();
		if (getConfig().contains("UsePermissions")) {
			this.usePermissions = getConfig().getBoolean("UsePermissions");
		} else {
			this.usePermissions = true;
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Warps has been successfully enabled!");
	}

	public FileBuilder getFileBuilder() {
		return this.fb;
	}

	public YamlConfiguration getWarps() {
		return this.warps;
	}

	public void saveWarps() {
		try {
			this.warps.save(this.w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setupYamls() {
		this.w = new File(getDataFolder(), "warps.yml");
		if (!this.w.exists())
			saveResource("warps.yml", false);
		this.warps = new YamlConfiguration();
		try {
			this.warps.load(this.w);
		} catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
			e.printStackTrace();
		}
		if (this.warps.contains("WarpList"))
			this.warps.set("WarpList", null);
	}

	public Utils getUtils() {
		return this.utils;
	}

	public Msgs getMsgs() {
		return this.msgs;
	}

	public boolean usePermissions() {
		return this.usePermissions;
	}
}
