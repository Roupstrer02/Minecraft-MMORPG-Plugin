package me.roupen.firstpluginthree.CraftingRecipes;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.artifacts.dreamerFriend;
import me.roupen.firstpluginthree.balance.Balance;
import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import java.util.ArrayList;
import java.util.HashMap;

public class SmithingRecipes {


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
    private ArrayList<Material> scrapMaterials;
    public SmithingRecipes() {

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
        this.scrapMaterials = new ArrayList<Material>() {{
            add(Material.ROTTEN_FLESH);
            add(Material.BONE);
            add(Material.STRING);
            add(Material.GUNPOWDER);
        }};

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

    private void initSmithingRecipes(ArrayList<Recipe> ListOfRecipes) {


        ItemStack scrap = giveEnchantedLook(newCraftableItem(Material.BONE_MEAL, 1, "Scrap", "One person's trash,", "is another person's treasure!"));
        ItemStack scrapB = giveEnchantedLook(newCraftableItem(Material.WHITE_DYE, 1, "Bunch of Scrap", "One person's treasure is...", "better treasure now?... I think?..."));
        ItemStack scrapC = giveEnchantedLook(newCraftableItem(Material.PHANTOM_MEMBRANE, 1, "Loads of Scrap", "Ok so now the treasure was already good so...", "At this point this expression boils down to theft, no?..."));
        ItemStack scrapD = giveEnchantedLook(newCraftableItem(Material.PAPER, 1, "Skill Reallocation Ticket", "I don't want to think about it anymore...", "Look. You win. No more scrap. Here's your treasure...","","Redeem this at spawn to reallocate your skill points"));


        this.newItem = scrap;
        //==================================================================================================================
        //Scrap
        for (Material mat : scrapMaterials) {
            nameKey = makeNameKey("scrap-" + mat.toString());

            newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

            newShapelessRecipe.addIngredient(9, mat);

            addToMaps(newItem.toString(), newItem, newShapelessRecipe);
            ListOfRecipes.add(newShapelessRecipe);
        }

        //==================================================================================================================
        //Scrap B
        this.newItem = scrapB;

        nameKey = makeNameKey("scrap-B");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        for (int i = 0; i < 4; i++)
            newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(scrap));


        addToMaps(newItem.toString(), newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==================================================================================================================
        //Scrap C
        this.newItem = scrapC;

        nameKey = makeNameKey("scrap-C");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        for (int i = 0; i < 4; i++)
            newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(scrapB));

        addToMaps(newItem.toString(), newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==================================================================================================================
        //Scrap D
        this.newItem = scrapD;

        nameKey = makeNameKey("scrap-D");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        for (int i = 0; i < 4; i++)
            newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(scrapC));

        addToMaps(newItem.toString(), newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

    }


    public void initRecipes() {

        ArrayList<Recipe> ListOfRecipes = new ArrayList<>();

        initSmithingRecipes(ListOfRecipes);

        //==========================================================================================================
        //END OF RECIPES
        //==========================================================================================================

        //Adding all the recipes defined above into the server
        for (Recipe recipe : ListOfRecipes) {
            FirstPluginThree.getMyPlugin().getServer().addRecipe(recipe);
        }

    }
}
