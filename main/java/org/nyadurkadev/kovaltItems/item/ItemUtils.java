package org.nyadurkadev.kovaltItems.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.nyadurkadev.kovaltItems.Main;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils implements CommandExecutor {

    public static ItemStack createWaterCan() {
        ItemStack waterCan = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = waterCan.getItemMeta();

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "WATERCAN");

        NamespacedKey keyWater = new NamespacedKey(Main.getInstance(), "water_level");
        meta.getPersistentDataContainer().set(keyWater, PersistentDataType.INTEGER, 0);
        meta.setCustomModelData(110);

        meta.setDisplayName("§fЛейка");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§fЗажмите §b«ПКМ» около §fпосевов");
        lore.add("§fдля ускорения их роста");
        lore.add("");
        lore.add("§fУровень воды в лейке: §b" + "0" + "/10");
        lore.add("");
        meta.setLore(lore);

        waterCan.setItemMeta(meta);

        return waterCan;
    }

    public static ItemStack createGardenGloves() {
        ItemStack gloves = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = gloves.getItemMeta();

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "GLOVES");
        meta.setCustomModelData(111);

        meta.setDisplayName("§aСадовые Перчатки");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§fПоложите перчатку в §aлевую руку");
        lore.add("§fИ начните собирать ягоды для");
        lore.add("§fполучения §aдополнительного §fдропа");
        lore.add("");
        meta.setLore(lore);

        gloves.setItemMeta(meta);

        return gloves;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда для игроков");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("getwatercan")) {
            player.getInventory().addItem(createWaterCan());
        }

        if (command.getName().equalsIgnoreCase("getgloves")) {
            player.getInventory().addItem(createGardenGloves());
        }

        return true;
    }
}
