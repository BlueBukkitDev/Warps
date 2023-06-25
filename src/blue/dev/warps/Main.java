package blue.dev.warps;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main
  extends JavaPlugin
{
  public static Main plugin;
  private File w;
  private File s;
  private YamlConfiguration warps;
  private YamlConfiguration signs;
  public BukkitScheduler scheduler;
  
  public void onEnable()
  {
    plugin = this;
    this.scheduler = getServer().getScheduler();
    setupYamls();
    Cmds cmds = new Cmds();
    getCommand("delwarp").setExecutor(cmds);
    getCommand("setwarp").setExecutor(cmds);
    getCommand("warp").setExecutor(cmds);
    getCommand("warps").setExecutor(cmds);
    Bukkit.getPluginManager().registerEvents(new SignListener(), this);
    Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
    saveDefaultConfig();
    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Warps has been successfully enabled!");
  }
  
  public YamlConfiguration getWarps()
  {
    return this.warps;
  }
  
  public YamlConfiguration getSigns()
  {
    return this.signs;
  }
  
  public void saveWarps()
  {
    try
    {
      this.warps.save(this.w);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public void saveSigns()
  {
    try
    {
      this.signs.save(this.s);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void setupYamls()
  {
    this.w = new File(getDataFolder(), "warps.yml");
    this.s = new File(getDataFolder(), "signs.yml");
    if (!this.w.exists()) {
      saveResource("warps.yml", false);
    }
    if (!this.s.exists()) {
      saveResource("signs.yml", false);
    }
    this.warps = new YamlConfiguration();
    this.signs = new YamlConfiguration();
    try
    {
      this.warps.load(this.w);
    }
    catch (IOException|InvalidConfigurationException e)
    {
      e.printStackTrace();
    }
    try
    {
      this.signs.load(this.s);
    }
    catch (IOException|InvalidConfigurationException e)
    {
      e.printStackTrace();
    }
  }
}
