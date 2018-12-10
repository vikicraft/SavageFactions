package com.massivecraft.factions.zcore.fupgrades;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class FUpgradesGUI implements Listener {
    public void openMainMenu(FPlayer fme) {
        Inventory inventory = Bukkit.createInventory(null, 27, P.p.color(P.p.getConfig().getString("fupgrades.MainMenu.Title").replace("{faction}", fme.getFaction().getTag())));
        List<Integer> dummySlots = P.p.getConfig().getIntegerList("fupgrades.MainMenu.DummyItem.slots");
        Material dummyMaterial = Material.getMaterial(P.p.getConfig().getString("fupgrades.MainMenu.DummyItem.Type"));
        int dummyAmount = P.p.getConfig().getInt("fupgrades.MainMenu.DummyItem.Amount");
        short dummyData = Short.parseShort(P.p.getConfig().getInt("fupgrades.MainMenu.DummyItem.Damage") + "");
        ItemStack dummyItem = P.p.createItem(dummyMaterial,
                dummyAmount,
                dummyData,
                P.p.color(P.p.getConfig().getString("fupgrades.MainMenu.DummyItem.Name")),
                P.p.colorList(P.p.getConfig().getStringList("fupgrades.MainMenu.DummyItem.Lore")));
        
        for (int i = 0; i <= dummySlots.size() - 1; i++) {
            inventory.setItem(dummySlots.get(i), dummyItem);
        }
        
        ItemStack[] items = buildItems(fme);
        List<Integer> cropSlots = P.p.getConfig().getIntegerList("fupgrades.MainMenu.Crops.CropItem.slots");
        List<Integer> spawnerSlots = P.p.getConfig().getIntegerList("fupgrades.MainMenu.Spawners.SpawnerItem.slots");
        List<Integer> expSlots = P.p.getConfig().getIntegerList("fupgrades.MainMenu.EXP.EXPItem.slots");
        List<Integer> chestSlots = P.p.getConfig().getIntegerList("fupgrades.MainMenu.Chest.ChestItem.slots");
        
        for (int i = 0; i < cropSlots.size(); i++)
            inventory.setItem(cropSlots.get(i), items[2]);

        for (int i = 0; i < spawnerSlots.size(); i++)
            inventory.setItem(spawnerSlots.get(i), items[1]);

        for (int i = 0; i < expSlots.size(); i++)
            inventory.setItem(expSlots.get(i), items[0]);

        for (int i = 0; i < chestSlots.size(); i++)
            inventory.setItem(chestSlots.get(i), items[3]);

        fme.getPlayer().openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getCursor() == null)
            return;
            
        FPlayer fme = FPlayers.getInstance().getByPlayer((Player) e.getWhoClicked());
        if (e.getClickedInventory().getTitle().equalsIgnoreCase(P.p.color(P.p.getConfig().getString("fupgrades.MainMenu.Title").replace("{faction}", fme.getFaction().getTag())))) {
            e.setCancelled(true);
            ItemStack[] items = buildItems(fme);
            ItemStack cropItem = items[2];
            ItemStack expItem = items[0];
            ItemStack chestitem = items[3];
            ItemStack spawnerItem = items[1];
            
            if (e.getCurrentItem().equals(cropItem)) {
            	int cropLevel = fme.getFaction().getUpgrade("Crop");
            	
            	switch (cropLevel)
            	{
            		case 3: return;
            		case 2: upgradeItem(fme, "Crop", 3, P.p.getConfig().getInt("fupgrades.MainMenu.Crops.Cost.level-3")); break;
            		case 1: upgradeItem(fme, "Crop", 2, P.p.getConfig().getInt("fupgrades.MainMenu.Crops.Cost.level-2")); break;
            		case 0: upgradeItem(fme, "Crop", 1, P.p.getConfig().getInt("fupgrades.MainMenu.Crops.Cost.level-1")); break;
            	}
            }
            else if (e.getCurrentItem().equals(spawnerItem)) {
            	int spawnerLevel = fme.getFaction().getUpgrade("Spawner");
            	
            	switch(spawnerLevel)
            	{
            		case 3: return;
            		case 2: upgradeItem(fme, "Spawner", 3, P.p.getConfig().getInt("fupgrades.MainMenu.Spawners.Cost.level-3")); break;
            		case 1: upgradeItem(fme, "Spawner", 2, P.p.getConfig().getInt("fupgrades.MainMenu.Spawners.Cost.level-2")); break;
            		case 0: upgradeItem(fme, "Spawner", 1, P.p.getConfig().getInt("fupgrades.MainMenu.Spawners.Cost.level-1")); break;
            	}	
            }
            else if (e.getCurrentItem().equals(expItem)) {
            	int expLevel = fme.getFaction().getUpgrade("Exp");
            	
            	switch (expLevel)
            	{
            		case 3: return;
            		case 2: upgradeItem(fme, "Exp", 3, P.p.getConfig().getInt("fupgrades.MainMenu.EXP.Cost.level-3")); break;
            		case 1: upgradeItem(fme, "Exp", 2, P.p.getConfig().getInt("fupgrades.MainMenu.EXP.Cost.level-2")); break;
            		case 0: upgradeItem(fme, "Exp", 1, P.p.getConfig().getInt("fupgrades.MainMenu.EXP.Cost.level-1")); break;
            	}
            }
            else if (e.getCurrentItem().equals(chestitem)) {
            	int chestLevel = fme.getFaction().getUpgrade("Chest");
            	
            	switch (chestLevel)
            	{
            		case 3: return;
            		case 2:
            		{   
            			if (upgradeItem(fme, "Chest", 3, P.p.getConfig().getInt("fupgrades.MainMenu.Chest.Cost.level-3")))
            				updateChests(fme.getFaction());
                        break;
            		}
            		case 1:
            		{
            			if (upgradeItem(fme, "Chest", 2, P.p.getConfig().getInt("fupgrades.MainMenu.Chest.Cost.level-2")))
            				updateChests(fme.getFaction());
                        break;
            		}
            		case 0:
            		{
            			if (upgradeItem(fme, "Chest", 1, P.p.getConfig().getInt("fupgrades.MainMenu.Chest.Cost.level-1")))
            				updateChests(fme.getFaction());
                        break;
            		}
            	}
            }
        }
    }

    private void updateChests(Faction faction) {
    	String invName = P.p.color(P.p.getConfig().getString("fchest.Inventory-Title"));
    	
        for (Player player : faction.getOnlinePlayers()) {
            if (player.getInventory().getTitle() != null && player.getInventory().getTitle().equalsIgnoreCase(invName))
            	player.closeInventory();
        }

        int level = faction.getUpgrade("Chest");
        int size = 1;
        
        switch (level)
        {
        	case 1: size = P.p.getConfig().getInt("fupgrades.MainMenu.Chest.Chest-Size.level-1"); break;
        	case 2: size = P.p.getConfig().getInt("fupgrades.MainMenu.Chest.Chest-Size.level-2"); break;
        	case 3: size = P.p.getConfig().getInt("fupgrades.MainMenu.Chest.Chest-Size.level-3"); break;
        }

        faction.setChestSize(size * 9);
    }

    private ItemStack[] buildItems(FPlayer fme) {
        Material expMaterial = Material.getMaterial(P.p.getConfig().getString("fupgrades.MainMenu.EXP.EXPItem.Type"));
        int expAmt = P.p.getConfig().getInt("fupgrades.MainMenu.EXP.EXPItem.Amount");
        short expData = Short.parseShort(P.p.getConfig().getInt("fupgrades.MainMenu.EXP.EXPItem.Damage") + "");
        String expName = P.p.color(P.p.getConfig().getString("fupgrades.MainMenu.EXP.EXPItem.Name"));
        List<String> expLore = P.p.colorList(P.p.getConfig().getStringList("fupgrades.MainMenu.EXP.EXPItem.Lore"));
        int expLevel = fme.getFaction().getUpgrade("Exp");
        
        for (int i = 0; i <= expLore.size() - 1; i++)
            expLore.set(i, expLore.get(i).replace("{level}", expLevel + ""));

        ItemStack expItem = P.p.createItem(expMaterial, expAmt, expData, expName, expLore);
        
        if (expLevel >= 1) {
            ItemMeta itemMeta = expItem.getItemMeta();
            if (!P.p.mc17) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemMeta.addEnchant(Enchantment.DURABILITY, 3, true);
            
            expItem.setItemMeta(itemMeta);
            expItem.setAmount(expLevel);
        }

        Material spawnerMaterial = Material.getMaterial(P.p.getConfig().getString("fupgrades.MainMenu.Spawners.SpawnerItem.Type"));
        int spawnerAmt = P.p.getConfig().getInt("fupgrades.MainMenu.Spawners.SpawnerItem.Amount");
        short spawnerData = Short.parseShort(P.p.getConfig().getInt("fupgrades.MainMenu.Spawners.SpawnerItem.Damage") + "");
        String spawnerName = P.p.color(P.p.getConfig().getString("fupgrades.MainMenu.Spawners.SpawnerItem.Name"));
        List<String> spawnerLore = P.p.colorList(P.p.getConfig().getStringList("fupgrades.MainMenu.Spawners.SpawnerItem.Lore"));

        List<Integer> spawnerSlots = P.p.getConfig().getIntegerList("fupgrades.MainMenu.Spawners.SpawnerItem.slots");
        int spawnerLevel = fme.getFaction().getUpgrade("Spawner");

        for (int i = 0; i <= spawnerLore.size() - 1; i++) {
            spawnerLore.set(i, spawnerLore.get(i).replace("{level}", spawnerLevel + ""));
        }
        
        Material cropMaterial = Material.getMaterial(P.p.getConfig().getString("fupgrades.MainMenu.Crops.CropItem.Type"));
        int cropAmt = P.p.getConfig().getInt("fupgrades.MainMenu.Crops.CropItem.Amount");
        short cropData = Short.parseShort(P.p.getConfig().getInt("fupgrades.MainMenu.Crops.CropItem.Damage") + "");
        String cropName = P.p.color(P.p.getConfig().getString("fupgrades.MainMenu.Crops.CropItem.Name"));
        List<String> cropLore = P.p.colorList(P.p.getConfig().getStringList("fupgrades.MainMenu.Crops.CropItem.Lore"));
        int cropLevel = fme.getFaction().getUpgrade("Crop");
        
        for (int i = 0; i <= cropLore.size() - 1; i++) {
            String line = cropLore.get(i);
            line = line.replace("{level}", cropLevel + "");
            cropLore.set(i, line);
        }
        
        ItemStack cropItem = P.p.createItem(cropMaterial, cropAmt, cropData, cropName, cropLore);
        cropItem.getItemMeta().setLore(cropLore);
        
        if (cropLevel >= 1) {
            ItemMeta itemMeta = cropItem.getItemMeta();
            if (!P.p.mc17) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemMeta.addEnchant(Enchantment.DURABILITY, 3, true);
            cropItem.setItemMeta(itemMeta);
            
            cropItem.setAmount(cropLevel);
        }
        
        ItemStack spawnerItem = P.p.createItem(spawnerMaterial, spawnerAmt, spawnerData, spawnerName, spawnerLore);
        spawnerItem.getItemMeta().setLore(spawnerLore);
        if (spawnerLevel >= 1) {
            ItemMeta itemMeta = spawnerItem.getItemMeta();
            if (!P.p.mc17) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemMeta.addEnchant(Enchantment.DURABILITY, 3, true);
            
            spawnerItem.setItemMeta(itemMeta);
            spawnerItem.setAmount(spawnerLevel);
        }
        
        Material chestMaterial = Material.getMaterial(P.p.getConfig().getString("fupgrades.MainMenu.Chest.ChestItem.Type"));
        int chesttAmt = P.p.getConfig().getInt("fupgrades.MainMenu.Chest.ChestItem.Amount");
        short chestData = Short.parseShort(P.p.getConfig().getInt("fupgrades.MainMenu.Chest.ChestItem.Damage") + "");
        String chestName = P.p.color(P.p.getConfig().getString("fupgrades.MainMenu.Chest.ChestItem.Name", "&e&lUpgrade Chest Size"));
        List<String> chestLore = P.p.colorList(P.p.getConfig().getStringList("fupgrades.MainMenu.Chest.ChestItem.Lore"));
        int chestlevel = fme.getFaction().getUpgrade("Chest");
        
        for (int i = 0; i <= chestLore.size() - 1; i++) {
            String line = chestLore.get(i);
            line = line.replace("{level}", chestlevel + "");
            chestLore.set(i, line);
        }

        ItemStack chestItem = P.p.createItem(chestMaterial, chesttAmt, chestData, chestName, chestLore);

        if (chestlevel >= 1) {
            ItemMeta itemMeta = chestItem.getItemMeta();
            if (!P.p.mc17) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemMeta.addEnchant(Enchantment.DURABILITY, 3, true);
            
            chestItem.setItemMeta(itemMeta);
            chestItem.setAmount(chestlevel);
        }

        ItemStack[] items = {expItem, spawnerItem, cropItem, chestItem};
        return items;
    }

    private boolean hasMoney(FPlayer fme, int amt) {
        return fme.hasMoney(amt);
    }

    private void takeMoney(FPlayer fme, int amt) {
        fme.takeMoney(amt);
    }
    
    private boolean upgradeItem(FPlayer fme, String upgrade, int level, int cost)
    {
        if (hasMoney(fme, cost)) {
        	takeMoney(fme, cost);
            fme.getFaction().setUpgrades(upgrade, level);
            fme.getPlayer().closeInventory();
            return true;
        }
        return false;
    }
}
