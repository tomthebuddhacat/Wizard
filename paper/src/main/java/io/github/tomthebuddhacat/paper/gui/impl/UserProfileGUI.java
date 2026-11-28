package io.github.tomthebuddhacat.paper.gui.impl;

import com.cryptomorin.xseries.XMaterial;
import io.github.tomthebuddhacat.common.record.PlayerRecord;
import io.github.tomthebuddhacat.common.record.RankRecord;
import io.github.tomthebuddhacat.common.repository.PlayerRepository;
import io.github.tomthebuddhacat.common.service.ItemService;
import io.github.tomthebuddhacat.common.service.RankService;
import io.github.tomthebuddhacat.paper.Wizard;
import io.github.tomthebuddhacat.paper.gui.InventoryButton;
import io.github.tomthebuddhacat.paper.gui.InventoryGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class UserProfileGUI extends InventoryGUI {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final PlayerRepository playerRepository;

    private final RankService rankService;

    private final ItemService itemService;

    private final Wizard plugin;

    private final int[] borderSlots = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 18, 27, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 17, 26, 35
    };

    public UserProfileGUI(PlayerRepository playerRepository, ItemService itemService, RankService rankService, Wizard plugin) {
        this.playerRepository = playerRepository;
        this.itemService = itemService;
        this.rankService = rankService;
        this.plugin = plugin;
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 45, MiniMessage.miniMessage().deserialize("User Profile"));
    }

    @Override
    public void decorate(Player player) {

        List<PlayerRecord> playerRecords = playerRepository.getAllInformation();

        RankRecord rankRecord = rankService.getPlayerRank(player.getUniqueId());

        for (int i = 0; i < playerRecords.size(); i++) {

            this.addButton(12, this.createProfileInformationButton(itemService.createItem(
                            XMaterial.BOOK.parseMaterial(),
                            rankRecord.rankPrefix().append(Component.text(player.getName()).color(rankRecord.tabNameColor().color())),
                            List.of(
                                    Component.empty(),
                                    miniMessage.deserialize("<gray><italic:false>Network Level:</gray> " + "<white><italic:false>" + playerRecords.get(i).networkLevel() + "</white>"),
                                    miniMessage.deserialize("<gray><italic:false>First Login:</gray> " + "<white><italic:false>" + playerRecords.get(i).firstLoginTimeStamp() + "</white>"),
                                    miniMessage.deserialize("<gray><italic:false>=Last Login:</gray> " + "<white><italic:false>" + playerRecords.get(i).lastLoginTimeStamp() + "</white>"),
                                    Component.empty()
                            )
                    ),
                    "player_information_skull"
            ));

            this.decorate(borderSlots, XMaterial.BLACK_STAINED_GLASS.parseItem());

            super.decorate(player);
        }
    }

    private InventoryButton createProfileInformationButton(ItemStack itemStack, String itemId) {

        NamespacedKey namespacedKey = new NamespacedKey(plugin, itemId);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, itemId);

        return new InventoryButton()
                .creator(player -> itemStack.clone())

                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();

                });
    }
}
