package io.github.tomthebuddhacat.gui;

import io.github.tomthebuddhacat.record.ReportRecord;
import io.github.tomthebuddhacat.repository.ReportRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TestGUI implements Listener {

    private final Inventory inventory;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final ReportRepository reportRepository;

    public TestGUI(ReportRepository reportRepository) {
        inventory = Bukkit.createInventory(null, 9, miniMessage.deserialize("<gray>Report Information</gray>"));
        this.reportRepository = reportRepository;
    }

    public void initializeItems() {

        List<ReportRecord> reportRecord = reportRepository.getAllDummyReports();

        if (reportRecord == null) return;

        for (int i = 0; i < inventory.getSize(); i++) {

            inventory.setItem(i, createGuiItem(
                    miniMessage.deserialize("<gray><italic:false>Report #" + i + "</gray>"),
                    Component.newline(),
                    miniMessage.deserialize("<gray><italic:false>Player Name:</gray> " + "<white>" + reportRecord.get(i).playerName() + "</white>"),
                    miniMessage.deserialize("<gray><italic:false>Reported Player Name:</gray> " + "<white>" +reportRecord.get(i).reportedPlayerName() + "</white>"),
                    miniMessage.deserialize("<gray><italic:false>Reported At:</gray> " + "<white>" + reportRecord.get(i).reportedTimeStamp() + "</white>")));

            break;
        }

    }

    protected ItemStack createGuiItem(Component name, Component... lore) {

        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(name);
        meta.lore(List.of(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(HumanEntity humanEntity) {
        initializeItems();
        humanEntity.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

        if (!inventoryClickEvent.getInventory().equals(inventory)) return;

        inventoryClickEvent.setCancelled(true);

        ItemStack clickedItem = inventoryClickEvent.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;
    }

    @EventHandler
    public void onInventoryClick(InventoryDragEvent inventoryDragEvent) {

        if (inventoryDragEvent.getInventory().equals(inventory)) {
            inventoryDragEvent.setCancelled(true);
        }
    }
}
