package me.roupen.firstpluginthree.artisan;

import me.roupen.firstpluginthree.FirstPluginThree;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BrewingStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;


/*
==================================================================================================================
Design Rule of Principle

No Custom Crafting menus, only vanilla
--> No brewing stands because of technical limitations (will look into that for alchemist update)

==================================================================================================================
 */
public class CookingRecipes {

    private HashMap<String, Recipe> Recipes;
    private HashMap<String, ItemStack> Items;
    private NamespacedKey nameKey;
    private ItemStack newItem;
    private ItemMeta newItemMeta;
    private String newItemName;
    private ArrayList<Component> newItemLore;
    private ShapedRecipe newShapedRecipe;
    private ShapelessRecipe newShapelessRecipe;
    private FurnaceRecipe newFurnaceRecipe;
    private SmokingRecipe newSmokingRecipe;
    public CookingRecipes() {

        this.Recipes = new HashMap<>();
        this.Items = new HashMap<>();

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
    private ItemStack newCraftableItem(ItemStack item, int amount, String name, String... lore) {
        newItemName = name;
        item.setAmount(amount);

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



    public void initRecipes() {

        ArrayList<Recipe> ListOfRecipes = new ArrayList<>();

        //==========================================================================================================
        //Example item

        newItem = new ItemStack(Material.DIAMOND, 1);

        newItemMeta = newItem.getItemMeta();
        newItemMeta.displayName(Component.text("test"));

        ItemStack bottle2 = newItem;
        bottle2.setItemMeta(newItemMeta);

        RecipeChoice namedBottle = new RecipeChoice.ExactChoice(bottle2);

        nameKey = makeNameKey("test");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("*%*","%B%","*%*");

        newShapedRecipe.setIngredient('*', namedBottle);
        newShapedRecipe.setIngredient('%', Material.SUGAR);
        newShapedRecipe.setIngredient('B', Material.GLASS_BOTTLE);

        FirstPluginThree.getMyPlugin().getServer().addRecipe(newShapedRecipe);

        //==========================================================================================================
        //Coffee Beans

        newItem = newCraftableItem(Material.SWEET_BERRIES, 1, "coffee beans","the unsweetened raw bean with incredible potential!");
        nameKey = makeNameKey("coffeebeans");
        newFurnaceRecipe = new FurnaceRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(new ItemStack(Material.SWEET_BERRIES, 1)), 0F, SecondsToTick(10));

        addToMaps(newItemName, newItem, newFurnaceRecipe);
        ListOfRecipes.add(newFurnaceRecipe);

        //==========================================================================================================
        //Blonde Roast

        newItem = newCraftableItem(Material.COCOA_BEANS, 1, "blonde roasted coffee beans","A lightly roasted coffee bean!");

        nameKey = makeNameKey("blonderoast");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("coffee beans")), 0F, SecondsToTick(10));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Medium Roast

        newItem = newCraftableItem(Material.COCOA_BEANS, 1, "medium roasted coffee beans","Roasted. but more!");

        nameKey = makeNameKey("mediumroast");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("blonde roasted coffee beans")), 0F, SecondsToTick(10));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Dark Roast

        newItem = newCraftableItem(Material.COCOA_BEANS, 1, "dark roasted coffee beans","Please stop roasting the beans...");
        newItemMeta = newItem.getItemMeta();
        newItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        newItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        newItem.setItemMeta(newItemMeta);
        nameKey = makeNameKey("darkroast");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("medium roasted coffee beans")), 0F, SecondsToTick(10));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Sweet Bread [Artisan Lv 15 locked]

        newItem = newCraftableItem(Material.BREAD, 1, "Sweet Bread","Bread but better!");

        nameKey = makeNameKey("sweetbread");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(3, Material.BREAD);
        newShapelessRecipe.addIngredient(1, Material.HONEY_BOTTLE);

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==========================================================================================================
        //Unfermented Mead [Artisan lv 10 locked]

        newItem = newCraftableItem(Material.HONEY_BOTTLE, 1, "Unfermented Mead", "The Precursor to a Dwarven Celebration");
        nameKey = makeNameKey("unfermentedmead");

        ItemStack meadIngredient = newPotionItem(new PotionData(PotionType.AWKWARD));

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","%B%","-%-");

        newShapedRecipe.setIngredient('%', Material.HONEYCOMB);
        newShapedRecipe.setIngredient('B', new RecipeChoice.ExactChoice(meadIngredient));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==========================================================================================================
        //Thorylin Mead

        newItem = newCraftableItem(Material.HONEY_BOTTLE, 1, "Thorylin Mead","Brothers of the Mine Rejoice!");
        newItemMeta = newItem.getItemMeta();
        newItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        newItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        newItem.setItemMeta(newItemMeta);
        nameKey = makeNameKey("thorylinmead");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Unfermented Mead")), 0F, MinutesToTick(5));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Beer Wort [Artisan Lv 18 locked]

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Beer Wort","It's almost ready lads!");
        nameKey = makeNameKey("beerwort");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(8, Material.WHEAT);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(newPotionItem(new PotionData(PotionType.WEAKNESS))));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==========================================================================================================
        //Embermoorian Mead

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Embermoorian Beer","A favorite amongst the workers of the Engine!");
        newItem = giveEnchantedLook(newItem);
        nameKey = makeNameKey("embermoorianbeer");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Beer Wort")), 0F, MinutesToTick(10));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Ithilian Fern Powder

        newItem = newCraftableItem(Material.GREEN_DYE, 1, "Ithilian Fern Powder","An essential ingredient for tea in Iliasing");
        nameKey = makeNameKey("ithilianfernpowder1");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, Material.FERN, 0F, SecondsToTick(30));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        newItem.setAmount(2);
        nameKey = makeNameKey("ithilianfernpowder2");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, Material.LARGE_FERN, 0F, SecondsToTick(45));

        addToMaps(newItemName + " 2", newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Lukewarm Tea Blend [Artisan Lv 8 Locked]

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Lukewarm Tea Blend","It's almost ready lads!");
        nameKey = makeNameKey("lukewarmteablend");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Ithilian Fern Powder")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(newPotionItem(new PotionData(PotionType.WATER))));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==========================================================================================================
        //Ithilian Pine Blend

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Ithilian Pine Blend","A drink fit for stargazing in Iliasing");
        newItem = giveEnchantedLook(newItem);
        nameKey = makeNameKey("ithilianpineblend");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Lukewarm Tea Blend")), 0F, MinutesToTick(1));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //END OF RECIPES
        //==========================================================================================================


        //============================================================================================================
        //Adding all of the recipes defined above into the server
        for (Recipe recipe : ListOfRecipes) {
            FirstPluginThree.getMyPlugin().getServer().addRecipe(recipe);
        }

    }
}
