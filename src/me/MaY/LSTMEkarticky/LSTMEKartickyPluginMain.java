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
	public static NamespacedKey qrMap;
	public static NamespacedKey tradingChest;
	@Override
	public void onEnable() {
        Utils.setMap(null);
		//Utils.setMap(Utils.loadFile());
		qrMap = new NamespacedKey(this,  "qrMap");
		tradingChest = new NamespacedKey(this,  "tradingChest");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new TradeItemsWithChestEvent(), this);
		pm.registerEvents(new ChatInputListener(), this);
		this.getCommand("change").setExecutor(new VymenaCommand());
		this.getCommand("tchest").setExecutor(new TradingChestCommand());



		System.out.println("LSTMEKarticky su tereaz funkcne, (onEnable)");
	}
	
	@Override
	public void onDisable() {
		
	}
	public static JavaPlugin getINSTANCE() {
		return JavaPlugin.getPlugin(LSTMEKartickyPluginMain.class);
	}
}
