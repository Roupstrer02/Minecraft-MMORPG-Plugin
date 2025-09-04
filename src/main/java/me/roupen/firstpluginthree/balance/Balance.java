package me.roupen.firstpluginthree.balance;

/*



 */

public class Balance {

    //------------------------------
    //---Global Scaling Variables---
    //------------------------------

//"diffMultiplierAtLevelDelta" is how many times harder it would be to fight a mob "levelDelta" levels over you vs a mob of your level

    public static int levelDelta = 8;
    public static int diffMultiplierAtLevelDelta = 2;


//Individual Stat Multipliers

    public static double mobDmgFactor = 3;
    public static double mobHpFactor = 10; //Adjust for display number readability (was 5 before Armen changed it 4:40pm, Aug 9)

    public static double weaponDmgFactor = 1; //Adjust for mob time to kill. ttk = 


//The following factors are to balance weapons based on stamina cost. They multiply
    public static double daggerMod = 2.0/3.0;
    public static double longswordMod = 1;
    public static double greatswordMod = 2.5;
    public static double shortbowMod = 7.0/6.0;
    public static double longbowMod = 2;
    public static double crossbowMod = 4.0/3.0;
    public static double craftedSwordMod = 8.0/9.0;
    public static double craftedAxeMod = 7.0/5.0;
    public static double craftedRangedWeaponMod = 4.0/5.0;

//Item Stats
    public static double helmDefMod = 0.8;
    public static double chestDefMod = 1.4;
    public static double legsDefMod = 1.0;
    public static double bootsDefMod = 0.8;
    public static double shieldDefMod = 1.0;

    public static double armourDefScale = 0.25;

    public static double wisdomFactor = 1; //Adjust this to tweak magic damage

    public static double wisdomSkillPointFraction = 0.75; //Fraction of skill points avg player will put into wisdom
    public static double avgWandWisMult = 2; //Wis multiplier from avg wand
    public static double XPFactor = 2026.355132762;
    public static double numOfMobsOfSameLevelToSlayForLvlUp = 20;
    public static double XPFromMobMod = 0; //~ -2 to 2. Tweaks how much more or less xp you get for killing mobs over or under your level. More - is less xp and more + is more xp. has no impact on mobs of same level as you
    public static double EXPGainCap = 0.10; // Experience gained towards next level is capped to this fractional value
    public static double BiomeLevelRange = 6.5;

    // player modifiers

    public static double playerBaseHealthAtLevel(int level) {
        return 3.4 * Math.pow(diffMultiplierAtLevelDelta, ((double) level) / levelDelta) * mobDmgFactor - 1 + 10;
    }

    public static double mobDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta, ((double) level) / (levelDelta)) * mobDmgFactor;
    }
    public static double mobHP(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * mobHpFactor;
    }
    public static double mobDef(int level) {
        return level - 1;
    }

    public static double craftedSwordDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * craftedSwordMod;
    }
    public static double craftedAxeDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * craftedAxeMod;
    }
    public static double craftedRangedWeaponDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * craftedRangedWeaponMod;
    }
    public static double daggerDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * daggerMod;
    }
    public static double longswordDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * longswordMod;
    }
    public static double greatswordDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * greatswordMod;
    }
    public static double shortbowDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * shortbowMod;
    }
    public static double longbowDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * longbowMod;
    }
    public static double crossbowDmg(int level) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta) * weaponDmgFactor * crossbowMod;
    }
    public static double helmDef(int level) {
        return ((double) level) * helmDefMod * armourDefScale;
    }
    public static double chestDef(int level) {
        return ((double) level) * chestDefMod * armourDefScale;
    }
    public static double legsDef(int level) {
        return ((double) level) * legsDefMod * armourDefScale;
    }
    public static double bootsDef(int level) {
        return ((double) level) * bootsDefMod * armourDefScale;
    }
    public static double shieldDef(int level) {
        return ((double) level) * shieldDefMod * armourDefScale;
    }
    public static double spellPowerCalc(int wisdom) {
        return Math.pow(diffMultiplierAtLevelDelta,((double) wisdom)/levelDelta) * wisdomFactor / (avgWandWisMult * wisdomSkillPointFraction);
    }
    public static int PlayerLevelCapEXPCalc(int level){
        return (int) Math.round((Math.pow(diffMultiplierAtLevelDelta,((double) level+1)/levelDelta) - Math.pow(diffMultiplierAtLevelDelta,((double) level)/levelDelta)) * XPFactor);
    }
    public static int MobExpRewardCalc(int mobLevel, int playerLevel){
        return (int) Math.round(((Math.pow(diffMultiplierAtLevelDelta,((double) mobLevel+1)/levelDelta) - Math.pow(diffMultiplierAtLevelDelta,((double) mobLevel)/levelDelta)) * XPFactor / (numOfMobsOfSameLevelToSlayForLvlUp * (1 + playerLevel / 100.0))) * Math.pow(((double) mobLevel)/playerLevel,XPFromMobMod));
    }
}
