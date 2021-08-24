package me.MaY.LSTMEkarticky.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.MaY.LSTMEkarticky.LSTMEKartickyPluginMain;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.storage.PersistentCommandStorage;

import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;

public class Utils {
	public static ArrayList<ItemStack> itemList;
    public static void summonSuccessRocket(Location playerLoc) {

        Firework fw = playerLoc.getWorld().spawn(playerLoc.add(0,3,0), Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
            
        meta.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
        fw.setFireworkMeta(meta);
    }

    public static String getTCGJson(int userid) throws IOException {
    	URL url = new URL("https://tcg.lstme.sk/api/submit_items");
    	URLConnection con = url.openConnection();
    	HttpURLConnection http = (HttpURLConnection)con;
    	http.setRequestMethod("POST");
    	http.setDoOutput(true);
    	byte[] out = ("{\"token\": \"" + userid + "\",\"stash_id\":\"" + LSTMEKartickyPluginMain.getINSTANCE().stashString + "\"}").getBytes(StandardCharsets.UTF_8);
    	int length = out.length;
    	http.setFixedLengthStreamingMode(length);
    	http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    	http.connect();
    	try(OutputStream os = http.getOutputStream()) {
    	    os.write(out);
    	}
    	try(InputStream is = http.getInputStream()) {
    	    return new BufferedReader(
    	    	      new InputStreamReader(is, StandardCharsets.UTF_8))
    	            .lines()
    	            .collect(Collectors.joining(""));
    	}
    }
    public static void performChangeChest(Player pl, String selector, Location chestLoc, int playerID) {
    	for(ItemStack entr: Utils.itemList) {
    		if(selector != null) {
    			if(!(entr.getType().name().contentEquals(selector)))
    				continue;
    		}
    		Chest chest = (Chest) chestLoc.getWorld().getBlockAt(chestLoc).getState();
	        if(!chest.getInventory().containsAtLeast(entr, entr.getAmount())) {
	        	continue;
	        }
	        chest.getSnapshotInventory().removeItem(new ItemStack(entr.getType(),entr.getAmount()));
	        chest.update(true);
	        Utils.summonSuccessRocket(pl.getLocation());
	        //request stuff here
	        try {
				String response = getTCGJson(playerID);
				JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
				if(obj.get("status").getAsString().equals("error")) {
					pl.sendMessage(ChatColor.RED + obj.get("error").getAsString());
					chest.getSnapshotInventory().addItem(new ItemStack(entr.getType(),entr.getAmount()));
			        chest.update(true);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
	        return;
    	}
    	pl.sendMessage(ChatColor.RED + "Nemas dostatok itemov");
    }
    public static boolean hasAtLeastOneTrade(Inventory inv) {
    	for(ItemStack entr: Utils.itemList) {
	        if(inv.containsAtLeast(entr, entr.getAmount())) {
	        	return true;
	        }
    	}
    	return false;
    }

    public static ArrayList<ItemStack> loadConfig(FileConfiguration config) {

    	ArrayList<ItemStack> list = new ArrayList<ItemStack>();
    	List<String> trades = config.getStringList("trades");
    	for(String tr : trades) {
    		String[] spl = tr.split(";");
    		if(spl.length != 2 || Material.matchMaterial(spl[0]) == null) {
    			LSTMEKartickyPluginMain.getINSTANCE().getLogger().warning("Failed to register " + tr);
    			continue;
    		}
    		list.add(new ItemStack(Material.matchMaterial(spl[0]), parseIntOrOne(spl[1])));
    		LSTMEKartickyPluginMain.getINSTANCE().getLogger().info("Registered " + tr);
    	}
    	

    	return list;
    }
    private static int parseIntOrOne(String toParse) {
    	try {
    		return Integer.parseInt(toParse);
    	}	catch(NumberFormatException e) {
    		return 1;
    	}
    }
}
