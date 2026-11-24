package io.github.tomthebuddhacat.gui.impl;

import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.gui.InventoryButton;
import io.github.tomthebuddhacat.gui.InventoryGUI;
import io.github.tomthebuddhacat.record.ReportRecord;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.ItemService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class TestReportGUI extends InventoryGUI {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final ReportRepository reportRepository;

    private final ItemService itemService;

    private final Wizard plugin;

    public TestReportGUI(ReportRepository reportRepository, ItemService itemService, Wizard plugin) {
        this.reportRepository = reportRepository;
        this.itemService = itemService;
        this.plugin = plugin;
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 3 * 9, MiniMessage.miniMessage().deserialize("<gray>Report Information</gray>"));
    }

    @Override
    public void decorate(Player player) {

        List<ReportRecord> reportRecord = reportRepository.getAllDummyReports();

        if (reportRecord == null || reportRecord.isEmpty()) {

                this.addButton(13, this.createReportButton(itemService.createItem(
                                Material.BARRIER,
                                miniMessage.deserialize("<red><italic:false>No Reports</red>"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>No players haven't been reported yet.</gray>")
                                )),
                        "there_are_no_reports",
                        -1
                ));

            super.decorate(player);

            return;
        }

        for (int i = 0; i < reportRecord.size(); i++) {

            this.addButton(i, this.createReportButton(itemService.createItem(
                    Material.BOOK,
                    miniMessage.deserialize("<gray><italic:false>Report #" + i + "</gray>"),
                    List.of(
                    Component.empty(),
                    miniMessage.deserialize("<gray><italic:false>Player Name:</gray> " + "<white><italic:false>" + reportRecord.get(i).playerName() + "</white>"),
                    miniMessage.deserialize("<gray><italic:false>Reported Player Name:</gray> " + "<white><italic:false>" +reportRecord.get(i).reportedPlayerName() + "</white>"),
                    miniMessage.deserialize("<gray><italic:false>Reported At:</gray> " + "<white><italic:false>" + reportRecord.get(i).reportedTimeStamp() + "</white>"),
                    miniMessage.deserialize("<gray><italic:false>Reported Reason:</gray> " + "<white><italic:false>" + reportRecord.get(i).reportReason() + "</white>"),
                    Component.empty(),
                    miniMessage.deserialize("<gray><italic:false>Right click to Reject Report</gray>"),
                    miniMessage.deserialize("<gray><italic:false>Left click to Accept Report</gray>")
                    )
            ),
            "there_are_reports",
            reportRecord.get(i).reportId()
            ));
        }
        super.decorate(player);
    }

    private InventoryButton createReportButton(ItemStack itemStack, String itemId, int reportId) {

        NamespacedKey namespacedKey = new NamespacedKey(plugin, itemId);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, itemId);

        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator(player -> itemStack.clone())

                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);

                    if (id == null) {
                        event.setCancelled(true);
                        return;
                    }

                    if ("there_are_no_reports".equals(id)) return;

                    if (event.getClick() == ClickType.RIGHT) {
                        reportRepository.updatePlayerReportStatusAsReject(reportId);
                        reportRepository.deleteDummyReport(reportId);
                        event.setCurrentItem(null);

                        player.sendMessage(miniMessage.deserialize("<yellow>You have successfully</yellow> <red><bold>REJECTED</bold></red> <yellow>a report with ID </yellow><red>" + reportId + ".</red>"));
                    }

                    if (event.getClick() == ClickType.LEFT) {
                        reportRepository.updatePlayerReportStatusAsAccepted(reportId);
                        reportRepository.deleteDummyReport(reportId);
                        event.setCurrentItem(null);

                        player.sendMessage(miniMessage.deserialize("<yellow>You have successfully</yellow> <green><bold>ACCEPTED</bold></green> <yellow>a report with ID </yellow><green>" + reportId + ".</green>"));
                    }
                });
    }
}
