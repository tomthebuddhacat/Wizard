package io.github.tomthebuddhacat.service;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemService {

    public ItemStack createItem(
            Material material,
            Component displayName,
            List<Component> itemLore) {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();;

        itemMeta.displayName(displayName);
        itemMeta.lore(itemLore);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
