package me.MaY.LSTMEkarticky;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.MaY.LSTMEkarticky.commands.TradingChestCommand;
import me.MaY.LSTMEkarticky.commands.VymenaCommand;
import me.MaY.LSTMEkarticky.events.ChatInputListener;
import me.MaY.LSTMEkarticky.events.TradeItemsWithChestEvent;
import me.MaY.LSTMEkarticky.utils.Utils;

public class LSTMEKartickyPluginMain extends JavaPlugin{
	public static NamespacedKey tradingChest;
	
	public String stashString;
	@Override
	public void onEnable() {
		tradingChest = new NamespacedKey(this,  "tradingChest");
		
		this.saveDefaultConfig();
		stashString = this.getConfig().getString("stash-string");
		Utils.itemList = Utils.loadConfig(getConfig());
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new TradeItemsWithChestEvent(), this);
		pm.registerEvents(new ChatInputListener(), this);
		this.getCommand("change").setExecutor(new VymenaCommand());
		this.getCommand("tchest").setExecutor(new TradingChestCommand());



		System.out.println("LSTMEKarticky su teraz funkcne, (onEnable)");
	}
	
	@Override
	public void onDisable() {
		
	}
	public static LSTMEKartickyPluginMain getINSTANCE() {
		return JavaPlugin.getPlugin(LSTMEKartickyPluginMain.class);
	}
}
