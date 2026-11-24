package io.github.tomthebuddhacat.gui.impl.test;

import com.cryptomorin.xseries.XMaterial;
import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.gui.InventoryButton;
import io.github.tomthebuddhacat.gui.InventoryGUI;
import io.github.tomthebuddhacat.record.ReportSession;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.ItemService;
import io.github.tomthebuddhacat.service.LogService;
import net.kyori.adventure.text.Component;
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

import java.util.List;

public class TestPlayerReportGUI extends InventoryGUI {

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

    public TestPlayerReportGUI(ReportRepository reportRepository, ItemService itemService, ReportSession reportSession, Wizard plugin) {
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

        for (int i = 0; i < itemSlots.length; i++) {

            switch (i) {

                case 0 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                        XMaterial.RED_CONCRETE.parseMaterial(),
                        miniMessage.deserialize("<italic:false>Unfair Advantage"),
                        List.of(
                                Component.empty(),
                                miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                miniMessage.deserialize("<gray><italic:false>using third-party software to gain"),
                                miniMessage.deserialize("<gray><italic:false>an unfair advantage.")
                        )
                ),
                        "report_unfair_advantage"
                ));

                case 1 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                XMaterial.RED_CONCRETE.parseMaterial(),
                                miniMessage.deserialize("<italic:false>Teaming"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                        miniMessage.deserialize("<gray><italic:false>teaming with others in"),
                                        miniMessage.deserialize("<gray><italic:false>game modes where it's not allowed.")
                                )
                        ),
                        "report_teaming"
                ));

                case 2 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                XMaterial.RED_CONCRETE.parseMaterial(),
                                miniMessage.deserialize("<italic:false>Chat Abuse"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                        miniMessage.deserialize("<gray><italic:false>using inappropriate or offensive"),
                                        miniMessage.deserialize("<gray><italic:false>language in chat.")
                                )
                        ),
                        "report_chat_abuse"
                ));

                case 3 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                XMaterial.RED_CONCRETE.parseMaterial(),
                                miniMessage.deserialize("<italic:false>IRL Trading or Boosting"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                        miniMessage.deserialize("<gray><italic:false>trading in-game items or currency"),
                                        miniMessage.deserialize("<gray><italic:false>for real-world money, or boosting.")
                                )
                        ),
                        "report_boosting"
                ));

                case 4 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                XMaterial.RED_CONCRETE.parseMaterial(),
                                miniMessage.deserialize("<italic:false>Advertising"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                        miniMessage.deserialize("<gray><italic:false>promoting other servers, websites,"),
                                        miniMessage.deserialize("<gray><italic:false>or services without permission.")
                                )
                        ),
                        "report_advertising"
                ));

                case 5 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                XMaterial.RED_CONCRETE.parseMaterial(),
                                miniMessage.deserialize("<italic:false>Punishment Evasion"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                        miniMessage.deserialize("<gray><italic:false>using alternate accounts to bypass"),
                                        miniMessage.deserialize("<gray><italic:false>server punishments.")
                                )
                        ),
                        "report_evasion"
                ));

                case 6 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                XMaterial.RED_CONCRETE.parseMaterial(),
                                miniMessage.deserialize("<italic:false>Exploiting"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                        miniMessage.deserialize("<gray><italic:false>abusing glitches or unintended"),
                                        miniMessage.deserialize("<gray><italic:false>game mechanics.")
                                )
                        ),
                        "report_exploiting"
                ));

                case 7 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                XMaterial.RED_CONCRETE.parseMaterial(),
                                miniMessage.deserialize("<italic:false>Inappropriate Username or Skin"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                        miniMessage.deserialize("<gray><italic:false>using offensive or inappropriate"),
                                        miniMessage.deserialize("<gray><italic:false>skins or usernames.")
                                )
                        ),
                        "report_inappropriate_profile"
                ));

                case 8 -> this.addButton(itemSlots[i], this.createReportButton(itemService.createItem(
                                XMaterial.RED_CONCRETE.parseMaterial(),
                                miniMessage.deserialize("<italic:false>Inappropriate Builds"),
                                List.of(
                                        Component.empty(),
                                        miniMessage.deserialize("<gray><italic:false>Report this player for"),
                                        miniMessage.deserialize("<gray><italic:false>creating offensive or inappropriate"),
                                        miniMessage.deserialize("<gray><italic:false>structures or builds.")
                                )
                        ),
                        "report_inappropriate_build"
                ));
            }
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

                    String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);

                    if (id == null) {
                        event.setCancelled(true);
                        return;
                    }

                    if (event.getClick() == ClickType.LEFT) {

                        if ("report_unfair_advantage".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "Unfair Advantage");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>Unfair Advantage.</red></bold>"));
                        }

                        if ("report_teaming".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "Teaming");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>Teaming.</red></bold>"));
                        }

                        if ("report_chat_abuse".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "Chat Abuse");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>Chat Abuse.</red></bold>"));
                        }

                        if ("report_boosting".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "IRL Trading or Boosting");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>IRL Trading or Boosting.</red></bold>"));
                        }

                        if ("report_advertising".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "Advertising");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>Advertising.</red></bold>"));
                        }

                        if ("report_evasion".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "Punishment Evasion");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>Punishment Evasion.</red></bold>"));
                        }

                        if ("report_exploiting".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "Exploiting Bugs or Features");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>Exploiting Bugs or Un-intended Features.</red></bold>"));
                        }

                        if ("report_inappropriate_profile".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "Inappropriate Skin or Name");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>Inappropriate Profile.</red></bold>"));
                        }

                        if ("report_inappropriate_build".equals(id)) {

                            reportRepository.createDummyReportData(player.getName(), reportSession.targetPlayer().getName(), "Inappropriate Builds");

                            event.getInventory().close();

                            player.sendMessage(miniMessage.deserialize("<yellow>You have successfully reported </yellow><red><bold>" + player.getName() + "</bold></red> <yellow>for</yellow> <bold><red>Inappropriate Builds.</red></bold>"));
                        }
                    }
                });
    }
}
