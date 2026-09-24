package org.nyadurkadev.kovaltItems.events;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;
import org.nyadurkadev.kovaltItems.Main;

import java.util.List;

public class WaterCan implements Listener {

    // система лейки
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack playerItem = event.getItem();

        String keyInHandID = null;
        if (playerItem != null && playerItem.hasItemMeta()) {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
            keyInHandID = playerItem.getItemMeta()
                    .getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) return;

        // поливка
        if ("WATERCAN".equals(keyInHandID)) {
            if (event.getAction().isRightClick() &&
                    (clickedBlock.getType() == Material.FARMLAND ||
                    clickedBlock.getBlockData() instanceof Ageable)) {

                if (player.hasCooldown(Material.AMETHYST_SHARD)) {
                    player.sendActionBar("§cПожалуйста, подождите перед следующим использованием!");
                    player.playSound(player.getLocation(),
                            Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }

                ItemMeta meta = playerItem.getItemMeta();

                NamespacedKey key = new NamespacedKey(Main.getInstance(), "water_level");
                int waterLevel = meta.getPersistentDataContainer()
                        .getOrDefault(key, PersistentDataType.INTEGER, 0);

                if (waterLevel > 0) {
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            Block plant = clickedBlock.getRelative(x, 0, z);

                            if (plant.getBlockData() instanceof Ageable ageable) {
                                if (Math.random() < 0.3) {
                                    if (ageable.getAge() < ageable.getMaximumAge()) {
                                        ageable.setAge(Math.min(ageable.getAge() + 2, ageable.getMaximumAge()));
                                        plant.setBlockData(ageable);
                                        plant.getWorld().spawnParticle(Particle.HAPPY_VILLAGER,
                                                plant.getLocation().add(0.5, 0.5, 0.5), 5);
                                    }
                                }
                            }
                        }
                    }
                    waterLevel--;
                    meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, waterLevel);

                    List<String> lore = meta.getLore();
                    lore.set(4, "§fУровень воды: §b" + waterLevel + "/10");
                    meta.setLore(lore);

                    playerItem.setItemMeta(meta);
                    player.playSound(player.getLocation(),
                            Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
                    player.setCooldown(Material.AMETHYST_SHARD, 50);
                } else {
                    player.sendActionBar("§cВ лейке недостаточно воды!");
                }
            }
        }
    }

    // для пополнения лейки
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCursor();
        ItemStack currentItem = event.getCurrentItem();

        Player player = (Player) event.getWhoClicked();

        if (currentItem == null || clickedItem == null) return;
        if (currentItem.getType().equals(Material.AIR) ||
                clickedItem.getType().equals(Material.AIR)) return;

        NamespacedKey currentKey = new NamespacedKey(Main.getInstance(), "key_id");
        String currentID = currentItem.getItemMeta().
                getPersistentDataContainer().get(currentKey, PersistentDataType.STRING);

        if (clickedItem.getType().equals(Material.POTION) &&
                "WATERCAN".equals(currentID)) {
            PotionMeta potionMeta = (PotionMeta) clickedItem.getItemMeta();
            PotionType potionType = potionMeta.getBasePotionType();

            if (potionType == PotionType.HEALING ||
                    potionType == PotionType.REGENERATION) {
                event.setCancelled(true);

                ItemMeta meta = currentItem.getItemMeta();

                NamespacedKey key = new NamespacedKey(Main.getInstance(), "water_level");
                int waterLevel = meta.getPersistentDataContainer()
                        .getOrDefault(key, PersistentDataType.INTEGER, 0);

                if (waterLevel >= 10) {
                    player.sendActionBar("§cЛейка заполнена!");
                    event.setCancelled(true);
                    return;
                }

                waterLevel = Math.min(waterLevel + 2, 10);
                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, waterLevel);
                player.sendActionBar("§bЛейка наполняется!");
                player.playSound(player.getLocation(),
                        Sound.ITEM_BOTTLE_FILL, 1.0f, 1.0f);

                List<String> lore = meta.getLore();
                lore.set(4, "§fУровень воды: §b" + waterLevel + "/10");
                meta.setLore(lore);

                currentItem.setItemMeta(meta);

                event.setCursor(null);

                player.updateInventory();
            }
        }
    }
}
