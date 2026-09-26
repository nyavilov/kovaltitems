package org.nyadurkadev.kovaltItems.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.nyadurkadev.kovaltItems.Main;
import org.nyadurkadev.kovaltItems.item.ItemUtils;

public class Recipe {
    public static void registerRecipes() {
        //крафт перчаток
        ItemStack gloves = ItemUtils.createGardenGloves();

        NamespacedKey glovesKey = new NamespacedKey(Main.getInstance(), "gloves");

        ShapedRecipe glovesRecipe = new ShapedRecipe(glovesKey, gloves);
        glovesRecipe.shape(
                "I I",
                "LIL",
                "S S");
        glovesRecipe.setIngredient('I', Material.IRON_INGOT);
        glovesRecipe.setIngredient('L', Material.LEATHER);
        glovesRecipe.setIngredient('S', Material.STRING);

        Bukkit.addRecipe(glovesRecipe);

        // крафт лейки
        ItemStack waterCan = ItemUtils.createWaterCan();

        NamespacedKey waterKey = new NamespacedKey(Main.getInstance(), "water");

        ShapedRecipe waterCanRecipe = new ShapedRecipe(waterKey, waterCan);
        waterCanRecipe.shape(
                "C C",
                "CBC",
                " C ");
        waterCanRecipe.setIngredient('C', Material.COPPER_INGOT);
        waterCanRecipe.setIngredient('B', Material.GLASS_BOTTLE);

        Bukkit.addRecipe(waterCanRecipe);
    }
}
