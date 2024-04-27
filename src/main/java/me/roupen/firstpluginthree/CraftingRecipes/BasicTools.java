package me.roupen.firstpluginthree.CraftingRecipes;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;

public class BasicTools {


    private HashMap<String, Recipe> Recipes;
    private HashMap<String, ItemStack> Items;
    private HashMap<String, Integer> ItemLevelLock;


    private NamespacedKey nameKey;
    private ItemStack newItem;
    private ItemMeta newItemMeta;
    private String newItemName;
    private ArrayList<Component> newItemLore;
    private ShapedRecipe newShapedRecipe;
    private ShapelessRecipe newShapelessRecipe;
    private FurnaceRecipe newFurnaceRecipe;
    private SmokingRecipe newSmokingRecipe;
    public BasicTools() {

        this.Recipes = new HashMap<>();
        this.Items = new HashMap<>();
        this.ItemLevelLock = new HashMap<>();

        //I honestly wrote this so that I stop getting suggestions from IntelliJ saying that I can localise these variables in initRecipes()
        this.newItem = null;
        this.newItemMeta = null;
        this.newItemName = "";
        this.newShapedRecipe = null;
        this.newShapelessRecipe = null;
        this.newFurnaceRecipe = null;
        this.newSmokingRecipe = null;
        this.newItemLore = new ArrayList<>();

    }

    public HashMap<String, Recipe> getRecipes() {
        return Recipes;
    }
    public HashMap<String, ItemStack> getItems() {
        return Items;
    }
    public HashMap<String, Integer> getItemLevelLock() {
        return ItemLevelLock;
    }
    private void addToLevelLockMap(String key, int minLevel) {
        ItemLevelLock.put(key, minLevel);
    }
    private NamespacedKey makeNameKey(String keyName) {
        return new NamespacedKey(FirstPluginThree.getMyPlugin(), keyName);
    }
    private void addToMaps(String name, ItemStack item, Recipe recipe) {
        Items.put(name, item);
        Recipes.put(name, recipe);
    }

    private int SecondsToTick(int seconds) {
        return 20 * seconds;
    }
    private int MinutesToTick(int minutes) {
        return 20 * 60 * minutes;
    }

    private ItemStack newCraftableItem(Material material, int amount, String name, String... lore) {
        newItemName = name;

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        ArrayList<Component> itemLore = new ArrayList<>();
        for (String line : lore) {
            itemLore.add(Component.text(line));
        }
        meta.lore(itemLore);
        item.setItemMeta(meta);

        return item;
    }
    private ItemStack newCraftableItem(ItemStack item, int amount, String name) {
        newItemName = name;

        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        item.setItemMeta(meta);

        return item;
    }
    private ItemStack newPotionItem(PotionData data) {
        ItemStack newPotion = new ItemStack(Material.POTION);
        PotionMeta pm = (PotionMeta) newPotion.getItemMeta();
        pm.setBasePotionData(data);
        newPotion.setItemMeta(pm);

        return newPotion;
    }
    private ItemStack giveEnchantedLook(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    private void initBasicTools(ArrayList<Recipe> ListOfRecipes) {
        PlayerEquipment newEquip;

        //==================================================================================================================
        //Stone Sword
        newEquip = new PlayerEquipment(0, Material.STONE_SWORD, "Longsword");
        newEquip.setDamage(10.0);
        newEquip.setStaminaCost(5.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-stone-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.COBBLESTONE);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Gold Sword
        newEquip = new PlayerEquipment(0, Material.GOLDEN_SWORD, "Longsword");
        newEquip.setDamage(5.0);
        newEquip.setStaminaCost(5.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-golden-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.GOLD_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Iron Sword
        newEquip = new PlayerEquipment(0, Material.IRON_SWORD, "Longsword");
        newEquip.setDamage(15.0);
        newEquip.setStaminaCost(5.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-iron-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.IRON_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Diamond Sword
        newEquip = new PlayerEquipment(0, Material.DIAMOND_SWORD, "Longsword");
        newEquip.setDamage(25.0);
        newEquip.setStaminaCost(5.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-diamond-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.DIAMOND);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Netherite Sword
        newEquip = new PlayerEquipment(0, Material.NETHERITE_SWORD, "Longsword");
        newEquip.setDamage(40.0);
        newEquip.setStaminaCost(5.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-netherite-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.NETHERITE_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);
    }


    public void initRecipes() {

        ArrayList<Recipe> ListOfRecipes = new ArrayList<>();

        initBasicTools(ListOfRecipes);

        //==========================================================================================================
        //END OF RECIPES
        //==========================================================================================================

        //Adding all of the recipes defined above into the server
        for (Recipe recipe : ListOfRecipes) {
            FirstPluginThree.getMyPlugin().getServer().addRecipe(recipe);
        }

    }
}
