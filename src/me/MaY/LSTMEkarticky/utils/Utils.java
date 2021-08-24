package me.MaY.LSTMEkarticky.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
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
import java.util.HashMap;
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

import me.MaY.LSTMEkarticky.LSTMEKartickyPluginMain;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.storage.PersistentCommandStorage;

import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;

public class Utils {
	private static HashMap<ItemStack, Integer> hsm = null;
    public static void summonSuccessRocket(Location playerLoc) {

        Firework fw = playerLoc.getWorld().spawn(playerLoc.add(0,3,0), Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
            
        meta.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
        fw.setFireworkMeta(meta);
    }

    public static void setMap(HashMap<ItemStack,Integer> hsm1)
    {
        if (hsm1 == null) {
            hsm = loadFile();
            /*hsm = new HashMap<ItemStack, Integer>();
            hsm.put(new ItemStack(Material.NETHERITE_INGOT, 2), 1);*/
            return;
        }
        hsm = hsm1;
    }

    public static HashMap<ItemStack, Integer> getItems() {
        if (hsm == null) hsm = loadFile();
        return hsm;
    }
    public static String getTCGJson(int userid) throws IOException {
    	URL url = new URL("http://ptsv2.com/t/vligm-1629755516/post");
    	URLConnection con = url.openConnection();
    	HttpURLConnection http = (HttpURLConnection)con;
    	http.setRequestMethod("POST");
    	http.setDoOutput(true);
    	byte[] out = ("{\"userid\": " + userid + "}").getBytes(StandardCharsets.UTF_8);
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
    	for(Entry<ItemStack,Integer> entr: Utils.getItems().entrySet()) {
    		if(selector != null) {
    			if(!(entr.getKey().getType().name().contentEquals(selector)))
    				continue;
    		}
    		Chest chest = (Chest) chestLoc.getWorld().getBlockAt(chestLoc).getState();
	        if(!chest.getInventory().containsAtLeast(entr.getKey(), entr.getKey().getAmount())) {
	        	continue;
	        }
	        chest.getSnapshotInventory().removeItem(new ItemStack(entr.getKey().getType(),entr.getKey().getAmount()));
	        chest.update(true);
	        Utils.summonSuccessRocket(pl.getLocation());
	        //request stuff here
	        
	        return;
    	}
    	pl.sendMessage(ChatColor.RED + "Nemas dostatok itemov");
    }

    public static HashMap<ItemStack, Integer> loadFile() {

    	HashMap<ItemStack, Integer> map = new HashMap<ItemStack, Integer>();

    	try
    	{
    		File file = new File("ceny.config");

    		if (!file.exists())
    		{
    			file.createNewFile();

    			FileWriter writer = new FileWriter(file);
    			writer.write("NETHERITE_INGOT;1//10\nDIAMOND_BLOCK;1//1");
    			writer.close();
    		}

    		Scanner scanner = new Scanner(file);

    		while (scanner.hasNextLine())
    		{
    			String next = scanner.nextLine();
    			String material = next.split("//")[0].split(";")[0];

    			int amt = Integer.parseInt(next.split("//")[0].split(";")[1]);
    			int kartickyAmt = Integer.parseInt(next.split("//")[1]);

    			ItemStack stack = new ItemStack(Material.matchMaterial(material), amt);

    			map.put(stack, kartickyAmt);
    		}

    		scanner.close();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}

    	return map;
    }
}
