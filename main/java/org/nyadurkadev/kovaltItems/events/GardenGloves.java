package org.nyadurkadev.kovaltItems.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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

    // для культур в виде блоков
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if (offHandItem == null || offHandItem.getType() == Material.AIR ||
                !offHandItem.hasItemMeta()) return;

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
        String keyInOffHandID = offHandItem.getItemMeta()
                .getPersistentDataContainer().get(key, PersistentDataType.STRING);

        Block block = event.getBlock();

        if ("GLOVES".equals(keyInOffHandID)) {
            if (block.getBlockData() instanceof Ageable ageable) {
                if (ageable.getAge() == ageable.getMaximumAge()) {

                    Material blockType = block.getType();
                    Material dropType = blockType;

                    if (blockType == Material.POTATOES) dropType = Material.POTATO;
                    if (blockType == Material.CARROTS) dropType = Material.CARROT;
                    if (blockType == Material.BEETROOTS) dropType = Material.BEETROOT;
                    if (blockType == Material.WHEAT) dropType = Material.WHEAT;

                    ItemStack bonus = new ItemStack(dropType, ((int) (Math.random() * 6) + 1));
                    block.getWorld().dropItemNaturally(block.getLocation(), bonus);

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
    
    // нельзя засовывать в слот ботинок
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack cursorItem = event.getCursor();

        // по слоту
        if (cursorItem != null && cursorItem.hasItemMeta()) {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
            String cursorItemKey = cursorItem.getItemMeta()
                    .getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if ("GLOVES".equals(cursorItemKey)) {
                if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                    event.setCancelled(true);
                }
            }
        }

        ItemStack clickedItem = event.getCurrentItem();

        // через шифт
        if (clickedItem != null && clickedItem.hasItemMeta()) {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
            String clickedItemKey = clickedItem.getItemMeta()
                    .getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if ("GLOVES".equals(clickedItemKey)) {
                if (event.isShiftClick()) {
                    event.setCancelled(true);
                }
            }
        }

        // через хотбар
        if (event.getClick().isKeyboardClick()) {
            int button = event.getHotbarButton();

            if (button >= 0) {
                ItemStack providedItem = player.getInventory().getItem(button);

                if (providedItem != null && providedItem.hasItemMeta()) {
                    NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
                    String providedItemKey = providedItem.getItemMeta()
                            .getPersistentDataContainer().get(key, PersistentDataType.STRING);

                    if ("GLOVES".equals(providedItemKey)) {
                        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    // попытка надеть через воздух
    @EventHandler
    public void onEquip(PlayerInteractEvent event) {
        ItemStack itemInHand = event.getItem();
        if (itemInHand == null || itemInHand.getType() == Material.AIR
                || !itemInHand.hasItemMeta()) return;

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
        String itemInHandKey = itemInHand.getItemMeta()
                .getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if ("GLOVES".equals(itemInHandKey)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                event.setCancelled(true);
            } else {
                event.setUseItemInHand(org.bukkit.event.Event.Result.DENY);
            }
            event.getPlayer().updateInventory();
        }
    }
    
    // метод для ягод
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack offHandItem = event.getItem();

        String keyInOffHandID = null;
        if (offHandItem == null || offHandItem.getType() == Material.AIR ||
                !offHandItem.hasItemMeta()) return;

        NamespacedKey key = new NamespacedKey(Main.getInstance(), "key_id");
        keyInOffHandID = offHandItem.getItemMeta()
                .getPersistentDataContainer().get(key, PersistentDataType.STRING);

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) return;

        // работа доп. дропа для ягод
        if ("GLOVES".equals(keyInOffHandID)) {
            event.setUseItemInHand(org.bukkit.event.Event.Result.DENY);
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
                            (int) (Math.random() * 6) + 1);
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
