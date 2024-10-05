package lewan24.profileStaticItem.Listeners;

import lewan24.profileStaticItem.main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public final class StaticItemListener implements Listener {
    final main plugin;
    private final Map<UUID, Long> commandCooldown = new HashMap<>();
    private final String mainPermission;
    private final boolean shouldItemBeStatic;
    private final NamespacedKey itemKey;

    private final String StaticItemKey = "Lewan24.StaticItemKey";

    public StaticItemListener(main plugin) {
        this.plugin = plugin;
        mainPermission = plugin.getConfig().getString("config.permissions", "main");
        shouldItemBeStatic = plugin.getConfig().getBoolean("config.shouldItemBeStatic", true);
        itemKey = new NamespacedKey(plugin, StaticItemKey);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!hasStaticItem(player)) {
            Inventory inventory = player.getInventory();
            ItemStack staticItem = createStaticItem();

            ItemStack itemInSlot8 = inventory.getItem(8);
            if (itemInSlot8 != null && !itemInSlot8.getType().isAir()) {
                int firstEmptySlot = inventory.firstEmpty();
                if (firstEmptySlot != -1) {
                    inventory.setItem(firstEmptySlot, itemInSlot8);
                } else {
                    player.getWorld().dropItem(player.getLocation(), itemInSlot8);
                }
            }

            inventory.setItem(8, staticItem);
            return;
        }

        removeAllStaticItems(player);

        Inventory inventory = player.getInventory();
        ItemStack staticItem = createStaticItem();

        inventory.setItem(8, staticItem);
    }

    private void removeAllStaticItems(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (!isStaticItem(item))
                continue;

            player.getInventory().remove(item);
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        ItemStack clickedItem = event.getItem();

        if (isStaticItem(clickedItem)) {
            PerformCommand(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();

        if (isStaticItem(itemStack)) {
            PerformCommand(event.getPlayer());
            event.setCancelled(true);
        }
    }

    private void PerformCommand(Player player) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (commandCooldown.containsKey(playerUUID)) {
            long lastCommandTime = commandCooldown.get(playerUUID);
            if (currentTime - lastCommandTime < 200) {
                return;
            }
        }

        String command = plugin.getConfig().getString("config.command", "profile");
        player.performCommand(command);
        commandCooldown.put(playerUUID, currentTime);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();

        if (!isStaticItem(currentItem) || !shouldItemBeStatic || player.hasPermission(mainPermission))
            return;

        event.setCancelled(true);

        if (player.getGameMode() == GameMode.CREATIVE){
            player.setItemOnCursor(null);
            player.closeInventory();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        drops.removeIf(this::isStaticItem);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.getInventory().setItem(8, createStaticItem());
    }

    private ItemStack createStaticItem() {
        String itemType = plugin.getConfig().getString("config.itemType", "NETHER_STAR");
        String itemName = plugin.getConfig().getString("config.itemName", "&6My Profile");
        List<String> itemLore = plugin.getConfig().getStringList("config.itemLore");

        Material material = Material.matchMaterial(itemType);
        if (material == null)
            material = Material.NETHER_STAR;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(getItemNameAsComponent(itemName));

            List<TextComponent> lore = new ArrayList<>();
            for (String line : itemLore)
                lore.add(getItemLoreLineAsComponent(line));

            meta.lore(lore);
            meta.setEnchantmentGlintOverride(plugin.getConfig().getBoolean("config.itemEnchant"));

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, StaticItemKey);

            item.setItemMeta(meta);
        }

        return item;
    }

    private boolean hasStaticItem(Player player) {
        for (ItemStack item : player.getInventory().getContents())
            if (isStaticItem(item))
                return true;

        return false;
    }

    private boolean isStaticItem(ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName())
            return false;

        if (meta.getPersistentDataContainer().has(itemKey, PersistentDataType.STRING)) {
            String id = meta.getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
            return StaticItemKey.equals(id);
        }

        return false;
    }

    public Component getItemNameAsComponent(String itemName){
        return LegacyComponentSerializer.legacyAmpersand().deserialize(itemName);
    }

    public TextComponent getItemLoreLineAsComponent(String itemLore){
        return LegacyComponentSerializer.legacyAmpersand().deserialize(itemLore);
    }
}
