package io.github.tomthebuddhacat.gui.impl;

import com.cryptomorin.xseries.XMaterial;
import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.enums.ReportType;
import io.github.tomthebuddhacat.gui.InventoryButton;
import io.github.tomthebuddhacat.gui.InventoryGUI;
import io.github.tomthebuddhacat.record.ReportSession;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.ItemService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerReportGUI extends InventoryGUI {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final ReportRepository reportRepository;

    private final ItemService itemService;

    private final Wizard plugin;

    private final ReportSession reportSession;

    private final int[] borderSlots = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 18, 27, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 17, 26, 35
    };

    private final int[] itemSlots = {
            11, 12, 13, 14, 15,
            20, 21, 22, 23, 24
    };

    public PlayerReportGUI(ReportRepository reportRepository, ItemService itemService, ReportSession reportSession, Wizard plugin) {
        this.reportRepository = reportRepository;
        this.itemService = itemService;
        this.reportSession = reportSession;
        this.plugin = plugin;
    }


    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 45, MiniMessage.miniMessage().deserialize("Report Menu"));
    }

    @Override
    public void decorate(Player player) {

        ReportType[] reportTypes = ReportType.values();

        for (int i = 0; i < reportTypes.length; i++) {

            ReportType reportType = reportTypes[i];

            this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                    XMaterial.RED_CONCRETE.parseMaterial(),
                                    miniMessage.deserialize("<italic:false>" + reportType.getReportReason()),
                                    reportType.getItemLore()
                            ),
                            reportType.getItemId())
            );
        }

        this.decorate(borderSlots, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());

        super.decorate(player);
    }

    private InventoryButton createReportButton(ItemStack itemStack, String itemId) {

        NamespacedKey namespacedKey = new NamespacedKey(plugin, itemId);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, itemId);

        itemStack.setItemMeta(itemMeta);

        return new InventoryButton()
                .creator(player -> itemStack.clone())

                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                    ItemStack currentItem = event.getCurrentItem();

                    if (currentItem == null) {
                        event.setCancelled(true);
                        return;
                    }

                    String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);

                    if (id == null) {
                        event.setCancelled(true);
                        return;
                    }

                    if (event.getClick() == ClickType.LEFT) {

                        ReportType reportType = null;

                        for (ReportType reportTypes : ReportType.values()) {

                            if (reportTypes.getItemId().equals(id)) {
                                reportType = reportTypes;
                                break;
                            }
                        }

                        if (reportType == null) {
                            event.setCancelled(true);
                            return;
                        }

                        reportRepository.createReport(
                                player,
                                reportSession.player(),
                                reportType.getReportReason()
                        );

                        event.getInventory().close();

                        player.sendMessage(miniMessage.deserialize("<green>You have successfully reported </green><red>" + reportSession.player().getName() + "</red><green>for</green> <red>" + reportType.getReportReason() + ".</red>"));
                    }
                });
    }
}