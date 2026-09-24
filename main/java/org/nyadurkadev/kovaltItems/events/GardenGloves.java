package org.nyadurkadev.kovaltItems.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;
import org.nyadurkadev.kovaltItems.Main;

public class GardenGloves implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        String keyInOffHandID = null;
        if (offHandItem != null &&
                offHandItem.getType() != Material.AIR &&
                offHandItem.hasItemMeta()) {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
            keyInOffHandID = offHandItem.getItemMeta()
                    .getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) return;

        if ("GLOVES".equals(keyInOffHandID)) {
            event.setCancelled(true);
            if (event.getAction().isRightClick() &&
                    clickedBlock.getType() == Material.SWEET_BERRY_BUSH) {
                if (clickedBlock.getBlockData() instanceof Ageable ageable &&
                        ageable.getAge() >= 2) {
                    double roll = Math.random();
                    Location blockLoc = clickedBlock.getLocation();

                    if (roll < 0.1) {
                        ItemStack diamond = new ItemStack(Material.DIAMOND,
                                (int) (Math.random() * 4) + 1);

                        clickedBlock.getWorld()
                                .dropItemNaturally(blockLoc, diamond);
                        player.sendActionBar("§aВы получили дополнительный лут!");
                    } else if (roll < 0.15) {
                        ItemStack potion = new ItemStack(Material.POTION, 1);
                        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                        potionMeta.setBasePotionType(PotionType.HEALING);

                        potion.setItemMeta(potionMeta);

                        clickedBlock.getWorld()
                                .dropItemNaturally(clickedBlock.getLocation(), potion);
                        player.sendActionBar("§aВы получили дополнительный лут!");
                    } else if (roll < 0.25) {
                        ItemStack gold = new ItemStack(Material.GOLD_NUGGET,
                                (int) (Math.random() * 6) + 1);

                        clickedBlock.getWorld()
                                .dropItemNaturally(blockLoc, gold);
                        player.sendActionBar("§aВы получили дополнительный лут!");
                    } else if (roll < 0.30) {
                        ItemStack berries = new ItemStack(Material.AMETHYST_SHARD,
                                (int) (Math.random() * 3) + 1);

                        clickedBlock.getWorld()
                                .dropItemNaturally(blockLoc, berries);
                        player.sendActionBar("§aВы получили дополнительный лут!");
                    }

                    ItemStack berries = new ItemStack(Material.SWEET_BERRIES,
                            (int) (Math.random() * 10) + 1);
                    clickedBlock.getWorld()
                            .dropItemNaturally(blockLoc, berries);

                    Damageable damageable = (Damageable) offHandItem.getItemMeta();

                    damageable.setDamage(damageable.getDamage() + 1);

                    if (damageable.getDamage() >= offHandItem.getType().getMaxDurability()) {
                        offHandItem.setAmount(0);
                        player.playSound(player.getLocation(),
                                Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                    } else {
                        offHandItem.setItemMeta(damageable);
                    }
                }
            }
        }
    }
}
