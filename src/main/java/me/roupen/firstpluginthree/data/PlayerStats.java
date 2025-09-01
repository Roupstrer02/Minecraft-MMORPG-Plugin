package me.roupen.firstpluginthree.data;


import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.balance.Balance;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Named;
import java.text.DecimalFormat;
import java.util.*;

public class PlayerStats {
    //Permanent Stats

    //Adds to max health
    private int Vitality;

    //Adds to defense
    private int Resilience;

    //Increases mana pool
    private int Intelligence;

    //Each point increases damage from Strength based weapons by 1% (additive)
    private int Strength;

    //Increases the damage of dexterity based weapons by 0.5% and improves movement speed by 0.5% (additive)
    private int Dexterity;

    //Increases mana pool and number of active spells at once (every few points)
    private int Wisdom;

    //Determines the player's ability to craft and create things from different fields (cooking, alchemy, etc...)
    private int Artisan;
    private int Experience;
    private int Level;

    //Number of unused skill points
    private int SkillPoints;

    //Total Stats (in-game)

    private Player player;
    public String currentBiomeGroup = "";
    public List<Double> HomeLocation = null;
    private double ActiveCurrentHealth;
    private double ActiveMaxHealth;
    private double ActiveDefense;
    private double ActiveDamage;
    private double ActiveCurrentMana;
    private double ActiveMaxMana;
    private double ActiveCurrentStamina;
    private double ActiveMaxStamina;
    private double MovementSpeed = 0.1;
    private double MultiHit;
    private double CritChance = 0.01;
    private double CritDamageMult = 1.5;
    private double StaminaCost = 1.0;

    //update & regen values
    private double ActiveStaminaRegen = 0.5;

    private double BaseActiveManaRegen = 0.125;
    private double baseActiveStaminaRegen = 0.5;

    //equipment related stats and values
    private PlayerEquipment equipment = new PlayerEquipment(0, Material.AIR, "");

    //Spellcasting related variables
    private boolean castingSpell = false;
    private String[] spellbook = new String[4];

    //consumable item related variables
    private boolean consumingItem = false;
    public int EquipmentLevelMinimum = 0;
    public boolean equippedUselessGear = false;

    //stat multipliers
    public Map<String, Double> LinearStatChanges = new HashMap<String, Double>() {{
        put("Max HP", 0.0);
        put("HP Regen", 0.0);
        put("Defense", 0.0);
        put("Damage", 0.0);
        put("Max Mana", 0.0);
        put("Mana Regen", 0.0);
        put("Stamina Cap", 0.0);
        put("Stamina Regen", 0.0);
        put("Multi Hit", 0.0);
        put("Crit Chance", 0.0);
        put("Crit Damage Mult", 0.0);
        put("Movement Speed", 0.0);
    }};

    public Map<String, Double> MultiplicativeStatChanges = new HashMap<String, Double>() {{
        put("Max HP", 1.0);
        put("HP Regen", 1.0);
        put("Defense", 1.0);
        put("Damage", 1.0);
        put("Max Mana", 1.0);
        put("Mana Regen", 1.0);
        put("Stamina Cap", 1.0);
        put("Stamina Regen", 1.0);
        put("Multi Hit", 1.0);
        put("Crit Chance", 1.0);
        put("Crit Damage Mult", 1.0);
        put("Movement Speed", 1.0);
    }};

    //constructor

    //Number Rounding
    private DecimalFormat df = new DecimalFormat("0.0");

    //Boss Fight related variables
    private boolean isInBossFight = false;
    //party related variables
    private Player pendingInvite;
    private ArrayList<Player> party = new ArrayList<>();

    //getter and setter and adder functions for everything
    public Player getPendingInvite() {
        return pendingInvite;
    }
    public void setPendingInvite(Player pendingInvite) {
        this.pendingInvite = pendingInvite;
    }
    public ArrayList<Player> getParty() {
        return party;
    }
    public void setParty(ArrayList<Player> party) {
        this.party = party;
    }
    public void addMemberToParty(Player player) {
        party.add(player);
    }
    public void removeMemberFromParty(Player player) {
        party.remove(player);
    }
    public Component getViewPartyMessage() {
        Component message = Component.text("Party Members: \n", Style.style(NamedTextColor.GREEN));
        for (Player p : party) {
            PlayerStats pStats = PlayerUtility.getPlayerStats(p);
            Block pLoc = p.getLocation().getBlock();
            message = message.append(
                    Component.text("- " + p.getName() + ": ", Style.style(NamedTextColor.GREEN)).append(
                            Component.text("Level " + pStats.getLevel() + "\n", Style.style(NamedTextColor.YELLOW)))).append(
                                    Component.text("Location: " + "x: " + pLoc.getX() + " y: " + pLoc.getY() + " z: " + pLoc.getZ() + "\n\n", Style.style(NamedTextColor.AQUA)));
        }
        return message;
    }

    public String[] getSpellbook() {
        return spellbook;
    }
    public void setSpellbook(String[] spellbook) {
        this.spellbook = spellbook;
    }
    public void updateSpellBook(int slot, String spell) {
        spellbook[slot] = spell;
    }

    public boolean isInBossFight() {
        return isInBossFight;
    }
    public void setInBossFight(boolean inBossFight) {
        isInBossFight = inBossFight;
    }
    public List<Double> getHomeLocation() {
        return HomeLocation;
    }
    public void setHomeLocation(List<Double> homeLocation) {
        HomeLocation = homeLocation;
    }
    public double getStaminaCost() {
        return StaminaCost;
    }
    public void setStaminaCost(double staminaCost) {
        StaminaCost = staminaCost;
    }
    public double getBaseActiveStaminaRegen() {
        return baseActiveStaminaRegen;
    }
    public void setBaseActiveStaminaRegen(double baseActiveStaminaRegen) {
        this.baseActiveStaminaRegen = baseActiveStaminaRegen;
    }
    public double getActiveDamage() {
        return ActiveDamage;
    }
    public void setActiveDamage(double activeDamage) {
        ActiveDamage = activeDamage;
    }
    public double getMultiHit() {
        return MultiHit;
    }
    public void setMultiHit(double multiHit) {
        MultiHit = multiHit;
    }
    public double getActiveMaxMana() {return ActiveMaxMana;}
    public void setActiveMaxMana(double activeMaxMana) {ActiveMaxMana = activeMaxMana;}
    public int getLevel() {
        return Level;
    }
    public void setLevel(int level) {
        Level = level;
    }
    public double getActiveCurrentHealth() {
        return ActiveCurrentHealth;
    }
    public void setActiveCurrentHealth(double activeCurrentHealth) {
        ActiveCurrentHealth = activeCurrentHealth;
    }
    public double getActiveMaxHealth() {
        return ActiveMaxHealth;
    }
    public void setActiveMaxHealth(double activeMaxHealth) {
        ActiveMaxHealth = activeMaxHealth;
    }
    public double getActiveDefense() {
        return ActiveDefense;
    }
    public void setActiveDefense(double activeDefense) {
        ActiveDefense = activeDefense;
    }
    public double getActiveCurrentMana() {
        return ActiveCurrentMana;
    }
    public void setActiveCurrentMana(double activeMana) {
        ActiveCurrentMana = activeMana;
    }
    public int getVitality() {
        return Vitality;
    }
    public void setVitality(int vitality) {
        Vitality = vitality;
    }
    public void addVitality(int addedvitality) {Vitality += addedvitality;}
    public int getResilience() {
        return Resilience;
    }
    public void setResilience(int defense) {
        Resilience = defense;
    }
    public int getArtisan() {
        return Artisan;
    }
    public void setArtisan(int artisan) {
        Artisan = artisan;
    }
    public void addArtisan(int addedartisan) {Artisan += addedartisan;}
    public double getMovementSpeed() {
        return MovementSpeed;
    }
    public void setMovementSpeed(double movementSpeed) {
        MovementSpeed = movementSpeed;
    }
    public double getMultihit() {
        return MultiHit;
    }
    public void setMultihit(double multihit) {
        MultiHit = multihit;
    }
    public double getCritChance() {
        return CritChance;
    }
    public void setCritChance(double critChance) {
        CritChance = critChance;
    }
    public double getCritDamageMult() {
        return CritDamageMult;
    }
    public void setCritDamageMult(double critDamageMult) {
        CritDamageMult = critDamageMult;
    }
    public double getActiveStaminaRegen() {
        return ActiveStaminaRegen;
    }
    public void setActiveStaminaRegen(double activeStaminaRegen) {
        ActiveStaminaRegen = activeStaminaRegen;
    }

    public double getBaseActiveManaRegen() {
        return BaseActiveManaRegen;
    }
    public void setBaseActiveManaRegen(double baseActiveManaRegen) {
        BaseActiveManaRegen = baseActiveManaRegen;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public boolean isCastingSpell() {
        return castingSpell;
    }
    public boolean hasConsumedItem() {return consumingItem; }
    public void setConsumingItem(boolean consuming) { consumingItem = consuming;}
    public void setCastingSpell(boolean castingSpell) {
        this.castingSpell = castingSpell;
    }
    public void AddEquipmentStats(PlayerEquipment e) {
        if (e.getLevel() <= this.Level) {
            equipment.setDamage(equipment.getDamage() + e.getDamage());
            equipment.setDefense(equipment.getDefense() + e.getDefense());
            equipment.setCritChance(equipment.getCritChance() + e.getCritChance());
            equipment.setMaxHealth(equipment.getMaxHealth() + e.getMaxHealth());
            equipment.setMaxMana(equipment.getMaxMana() + e.getMaxMana());
            equipment.setMaxStamina(equipment.getMaxStamina() + e.getMaxStamina());
            equipment.setHealthRegen(equipment.getHealthRegen() + e.getHealthRegen());
            equipment.setStaminaRegen(equipment.getStaminaRegen() + e.getStaminaRegen());
            equipment.setManaRegen(equipment.getManaRegen() + e.getManaRegen());
            equipment.setMultiHit(equipment.getMultiHit() + e.getMultiHit());
            equipment.setMovementSpeed(equipment.getMovementSpeed() + e.getMovementSpeed());
            equipment.setCritDamageMult(equipment.getCritDamageMult() + e.getCritDamageMult());

        }
    }
    public PlayerEquipment getEquipment() {
        return equipment;
    }
    public void setEquipment(PlayerEquipment equipment) {
        this.equipment = equipment;
    }
    public void updateEquipment(Player p) {

        equipment = new PlayerEquipment(0, Material.AIR, "");
        PlayerEquipment TempEquipment;

        equippedUselessGear = false;

        //updates this playerStats object with the armor of the inputted player (the player in question)
        PlayerInventory inventory = p.getInventory();
        ItemStack MainHand = inventory.getItemInMainHand();
        ItemStack OffHand = inventory.getItemInOffHand();
        ItemStack Helmet = inventory.getHelmet();
        ItemStack Chestplate = inventory.getChestplate();
        ItemStack Leggings = inventory.getLeggings();
        ItemStack Boots = inventory.getBoots();


        ItemStack[] AllItems = {MainHand, OffHand, Helmet, Chestplate, Leggings, Boots};

        //==========================
        //Sum up relevant stats of all equipment, accounting for playstyles (dual blades, sword & shield, greatsword)
        for (int i = 0; i < AllItems.length; i++)
        {


            if (AllItems[i] != null && AllItems[i].getType() != Material.AIR) {
                TempEquipment = PlayerEquipment.ItemToEquipment(AllItems[i]);

                //check if player is equipping any gear that is too high level
                if (TempEquipment.getLevel() > Level) {
                    equippedUselessGear = true;
                }

                //Special use case for double daggers
                if ((MainHand.getType() != Material.AIR && OffHand.getType() != Material.AIR) && TempEquipment.isDagger() && PlayerEquipment.ItemToEquipment(MainHand).isDagger() && PlayerEquipment.ItemToEquipment(OffHand).isDagger())
                {
                    TempEquipment.setDamage(0.75 * TempEquipment.getDamage());
                }

                //Special use case for two-handing a Greatsword
                else if (TempEquipment.isGreatSword() && OffHand.getType() == Material.AIR) {
                    TempEquipment.setDamage(1.5 * TempEquipment.getDamage());
                }

                //Special use case for sword and shield
                else if (OffHand.getType() == Material.SHIELD && TempEquipment.isLongSword()) {

                    TempEquipment.setDamage(1.4 * TempEquipment.getDamage());
                }
                else if (TempEquipment.getToolType().equals("Shield") && PlayerEquipment.ItemToEquipment(MainHand).isLongSword()) {

                    TempEquipment.setDefense(1.1 * TempEquipment.getDefense());

                }

                //adds offhand stats excluding damage and cost
                else if (AllItems[i] == OffHand)
                {
                    TempEquipment.setDamage(0.0);
                    TempEquipment.setStaminaCost(0.0);

                    //only get more defensive stats if the OffHand item is a shield (holding a helmet doesn't do much for you)
                    if (OffHand.getType() != Material.SHIELD) {
                        TempEquipment.setDefense(0.0);
                    }
                }

                //sets stamina cost of attacks
                if (AllItems[i] == MainHand) {
                    equipment.setStaminaCost(TempEquipment.getStaminaCost());
                    if (MainHand.getType() != Material.SHIELD) {
                        TempEquipment.setDefense(0.0);
                    }
                }

                if (AllItems[i] == MainHand || AllItems[i] == OffHand) {
                    //holding armor shouldn't help at all
                    if (TempEquipment.getToolType().equals("Helmet") || TempEquipment.getToolType().equals("Chestplate") || TempEquipment.getToolType().equals("Leggings") || TempEquipment.getToolType().equals("Boots")) {
                        TempEquipment = new PlayerEquipment(0, Material.AIR, "");
                    }
                }

                TempEquipment = TempEquipment.applyRunes();
                AddEquipmentStats(TempEquipment);
            }

        }
        //==========================
        //if shield is up
        if (p.isBlocking()) {
            if (MainHand.getType() == Material.SHIELD) {
                equipment.setDefense(equipment.getDefense() * (1 + (PlayerEquipment.ItemToEquipment(MainHand).getLevel() * 0.01)));
            }else {
                equipment.setDefense(equipment.getDefense() * (1 + (PlayerEquipment.ItemToEquipment(OffHand).getLevel() * 0.01)));
            }
        }

        //==========================
        //stat modifications from potion effects
        applyPotionEquipmentModifiers(p);
    }

    public void applyPotionEquipmentModifiers(Player p) {
        //can it scale off of the level of the effect easily?
        p.getActivePotionEffects();
        if (p.hasPotionEffect(PotionEffectType.POISON)) {
            equipment.setDefense(equipment.getDefense() * 0.6);
        }
        if (p.hasPotionEffect(PotionEffectType.WITHER)) {
            equipment.setStaminaCost(1 + equipment.getStaminaCost() * 1.5);
        }
        if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            equipment.setDamage(equipment.getDamage() * 1.25);
            equipment.setCritChance(equipment.getCritChance() + 0.05);
        }
        if (p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            equipment.setDefense(equipment.getDefense() * 1.25);
        }
        if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
            equipment.setDefense(equipment.getDefense() * 0.8);
            equipment.setDamage(equipment.getDamage() * 0.7);
        }

    }
    public void addResilience(int addedresilience){Resilience+=addedresilience;}
    public int getIntelligence() {
        return Intelligence;
    }
    public void setIntelligence(int mana) {
        Intelligence = mana;
    }
    public void addIntelligence(int addedintelligence){Intelligence+=addedintelligence;}
    public int getStrength() {return Strength;}
    public void setStrength(int strength) {Strength = strength;}
    public void addStrength(int addedstrength) {Strength += addedstrength;}
    public int getSkillPoints() {return SkillPoints;}
    public void setSkillPoints(int skillPoints) {SkillPoints = skillPoints;}
    public void addSkillPoints(int addedskillpoints) {SkillPoints += addedskillpoints;}
    public int getDexterity() {return Dexterity;}
    public void setDexterity(int dexterity) {Dexterity = dexterity;}
    public void addDexterity(int addeddexterity) {Dexterity += addeddexterity;}
    public int getWisdom() {return Wisdom;}
    public void setWisdom(int wisdom) {Wisdom = wisdom;}
    public void addWisdom(int addedwisdom) {Wisdom += addedwisdom;}
    public double getActiveCurrentStamina() {
        return ActiveCurrentStamina;
    }
    public void setActiveCurrentStamina(double activeCurrentStamina) {
        ActiveCurrentStamina = activeCurrentStamina;
    }
    public void useStamina(double amount) {
        //this function will have an equipment parameter as input
        setActiveCurrentStamina(getActiveCurrentStamina() - amount);
    }
    public void spendMana(double amount) {
        setActiveCurrentMana(getActiveCurrentMana() - amount);
    }
    public void useMana(double amount){
        setActiveCurrentMana(getActiveCurrentMana() - amount);
    }
    public double getActiveMaxStamina() {
        return ActiveMaxStamina;
    }
    public void setActiveMaxStamina(double activeMaxStamina) {
        ActiveMaxStamina = activeMaxStamina;
    }
    public int getExperience() {
        return Experience;
    }
    public void setExperience(int experience) {
        Experience = experience;
    }
    public void gainExperience(int exp) {
        //value subject to change
        int Levelcap = getLevelCap();
        this.Experience += exp;

        if (this.Experience >= Levelcap)
        {

            while (this.Experience >= Levelcap) {
                this.Experience -= Levelcap;
                this.Level += 1;
                Levelcap = getLevelCap();
                this.SkillPoints += 1;
            }
            getPlayer().chat(" Leveled up to level " + this.Level + "!");
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        }
    }

    public double getHealingReceivedModifier() {
        return 1 + (0.01 * (Resilience - 1));
    }

    public double getActiveHealthRegen() {
        return (((((ActiveMaxHealth / 80.0) + (Resilience - 1) * (ActiveMaxHealth / 11200.0)) + equipment.getHealthRegen()) + LinearStatChanges.get("HP Regen")) * MultiplicativeStatChanges.get("HP Regen"));
    }

    public double getActiveManaRegen() {
        return ((BaseActiveManaRegen + equipment.getManaRegen()) + LinearStatChanges.get("Mana Regen")) * MultiplicativeStatChanges.get("Mana Regen")   ;
    }

    public double getCasterSpellDamage() {
        return Balance.spellPowerCalc(this.Wisdom);
    }

    public double getCasterSpellDamage(double wisdomDelta) {
        return Balance.spellPowerCalc((int) Math.max(this.Wisdom + wisdomDelta, 1));
    }
    //Stat calculator for each stat
    public void recalculateMaxHealth() {
        setActiveMaxHealth(Double.parseDouble(df.format((
                Math.min(Double.MAX_VALUE, 1 + (0.01 * (getVitality() - 1))) * ((Balance.playerBaseHealthAtLevel(getLevel()) + equipment.getMaxHealth()) + LinearStatChanges.get("Max HP")) * MultiplicativeStatChanges.get("Max HP")))));
    }//Vitality
    public void recalculateHealth() {
        if (getActiveCurrentHealth() < getActiveMaxHealth()) {
            setActiveCurrentHealth(Math.min(getActiveMaxHealth(), getActiveCurrentHealth() + getActiveHealthRegen()));
        }
        else
        {
            setActiveCurrentHealth(getActiveMaxHealth());
        }
    }
    public void recalculateDamage() {
        if (equipment.getDamage() != 0)
            setActiveDamage(Double.parseDouble(df.format(MultiplicativeStatChanges.get("Damage") * ((getStrength() + (equipment.getDamage() * (1 + (0.01 * getStrength())))) + LinearStatChanges.get("Damage")))));
        else
            setActiveDamage(0.2);

    }//Strength
    public void recalculateCritDamageMult(){
        setCritDamageMult(Double.parseDouble(df.format(((1.5 + ((0.01 * getStrength()) * equipment.getCritDamageMult())) + LinearStatChanges.get("Crit Damage Mult")) * MultiplicativeStatChanges.get("Crit Damage Mult"))));
    }//Strength
    public void recalculateDefense() {
        setActiveDefense(Double.parseDouble(df.format(MultiplicativeStatChanges.get("Defense") * (equipment.getDefense() + LinearStatChanges.get("Defense")))));
    }
    public void recalculateMaxMana() {
        setActiveMaxMana(Double.parseDouble(df.format(MultiplicativeStatChanges.get("Max Mana") * (20 + ((getIntelligence() - 1) * 8) + equipment.getMaxMana() + LinearStatChanges.get("Max Mana")))));
    }//Intelligence
    public void recalculateMovementSpeed(Player p){
        setMovementSpeed((0.1 + (0.001 * (getDexterity() - 1)) + equipment.getMovementSpeed() + LinearStatChanges.get("Movement Speed")) * MultiplicativeStatChanges.get("Movement Speed"));
        p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getMovementSpeed());
    }//Dexterity
    public void recalculateMana() {
        if (getActiveCurrentMana() < getActiveMaxMana()) {
            setActiveCurrentMana(Math.min(getActiveMaxMana(), getActiveCurrentMana() + getActiveManaRegen()));
        }
        else
        {
            setActiveCurrentMana(getActiveMaxMana());
        }
    }
    public void recalculateStamina() {
        setActiveStaminaRegen((baseActiveStaminaRegen + equipment.getStaminaRegen() + LinearStatChanges.get("Stamina Regen")) * MultiplicativeStatChanges.get("Stamina Regen"));

        if (getActiveCurrentStamina() < getActiveMaxStamina()) {
            setActiveCurrentStamina(Math.min(getActiveMaxStamina(), getActiveCurrentStamina() + ActiveStaminaRegen));
        }
        else
        {
            setActiveCurrentStamina(getActiveMaxStamina());
        }
    }
    public void recalculateStaminaCost() {
        setStaminaCost(Double.parseDouble(df.format(1.0 + equipment.getStaminaCost())));
    }
    public void recalculateMaxStamina() {
        //this function will have a player equipment input eventually to add-on to the calculations
        setActiveMaxStamina(Double.parseDouble(df.format(MultiplicativeStatChanges.get("Stamina Cap") * (20.0 + equipment.getMaxStamina() + LinearStatChanges.get("Stamina Cap")))));
    }//None
    public void recalculateMultihit(){
        setMultihit(
                Double.parseDouble(df.format(
                        ((0.01 * getDexterity()) + equipment.getMultiHit() + LinearStatChanges.get("Multi Hit")) * MultiplicativeStatChanges.get("Multi Hit"))));
    } //Dexterity
    public void recalculateCritChance(){
        setCritChance(Double.parseDouble(df.format((0.01 + equipment.getCritChance() + LinearStatChanges.get("Crit Chance")) * MultiplicativeStatChanges.get("Crit Chance"))));
    }
    //==================================================================================
    //Stat Multiplier handlers

    public void changeLinearStats(String statName, double value) {
        LinearStatChanges.put(statName, LinearStatChanges.get(statName) + value);
    }

    public void changeMultiplicativeStats(String statName, double value) {
        MultiplicativeStatChanges.put(statName, MultiplicativeStatChanges.get(statName) * value);
    }
    //==================================================================================
    public void damage(MobStats mobstats) {
        ActiveCurrentHealth -= mobstats.getAttack() - (mobstats.getAttack() * (getActiveDefense() / (getActiveDefense() + 100)));
    }

    public void damage(MobStats mobstats, double mult_factor) {
        double damage_taken = mobstats.getAttack() * mult_factor;
        ActiveCurrentHealth -= damage_taken - (damage_taken * (getActiveDefense() / (getActiveDefense() + 100)));
    }

    public void trueDamage(MobStats mobstats) {
        ActiveCurrentHealth -= mobstats.getAttack();
    }

    public void trueDamage(MobStats mobstats, double mult_factor) {
        ActiveCurrentHealth -= mobstats.getAttack() * mult_factor;
    }

    public void respawnStatReset() {
        setActiveCurrentHealth(getActiveMaxHealth());
        setActiveCurrentStamina(0);
        setActiveCurrentMana(0);
    }
    public int getLevelCap() {
        return Balance.PlayerLevelCapEXPCalc(this.Level);
    }
    public void heal(double amount) {
        ActiveCurrentHealth += amount * getHealingReceivedModifier();
    }
    public Double getHomeDimension() {
        return HomeLocation.get(3);
    }
    public Double getPlayerDimensionID() {
        String dimension = player.getWorld().getEnvironment().toString();
        double dimension_id;
        switch (dimension) {
            case "NORMAL":
                dimension_id = 0;
                break;

            case "NETHER":
                dimension_id = 1;
                break;

            case "THE_END":
                dimension_id = 2;
                break;

            default:
                dimension_id = -1;
        }
        return dimension_id;
    }

    public void updateHomeLocation() {
        Location location = player.getLocation();
        String dimension = location.getWorld().getEnvironment().toString();
        double dimension_id;

        switch (dimension) {
            case "NORMAL":
                dimension_id = 0;
                break;

            case "NETHER":
                dimension_id = 1;
                break;

            case "THE_END":
                dimension_id = 2;
                break;

            default:
                dimension_id = -1;

        }

        //HomeLocation = Arrays.asList(location.getX(), location.getY(), location.getZ());
        player.sendMessage(dimension);
        HomeLocation = Arrays.asList(location.getX(), location.getY(), location.getZ(), dimension_id);
    }

}
