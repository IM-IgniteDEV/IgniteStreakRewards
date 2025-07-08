package com.ignitedev.igniteStreakRewards.util;

import com.ignitedev.aparecium.util.text.TextUtility;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;

import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ItemUtility {

  @Autowired private static StreakRewardsConfiguration configuration;

  public int getDailyIndex(ItemMeta itemMeta, String tag, String displayName) {
    int dailyIndex = 0;

    if (itemMeta == null || itemMeta.getLore() == null) {
      return 0;
    }
    for (String string : itemMeta.getLore()) {
      string = TextUtility.removeColor(string);

      if (!string.contains(tag)) {
        continue;
      }
      dailyIndex = TextUtility.parseStringIntegers(displayName.split(tag)[1])[0];
      break;
    }
    return dailyIndex;
  }

  public ItemStack getItemStackWithName(ItemStack itemStack, String name) {
    ItemMeta itemMeta = itemStack.getItemMeta();

    if (itemMeta != null) {
      itemMeta.setDisplayName(TextUtility.colorize(name));
      itemStack.setItemMeta(itemMeta);
    }
    return itemStack;
  }

  @NotNull
  public ItemStack getNotEnchantedItem(ItemStack itemStack) {
    ItemStack itemWithoutEnchantment = new ItemStack(itemStack.getType(), itemStack.getAmount());
    ItemMeta itemMetaWithoutEnchantment = itemWithoutEnchantment.getItemMeta();

    if (itemMetaWithoutEnchantment == null) {
      return voidItem();
    }
    ItemMeta itemMeta = itemStack.getItemMeta();

    if (itemMeta != null) {
      itemMetaWithoutEnchantment.setDisplayName(itemMeta.getDisplayName());
      itemMetaWithoutEnchantment.setLore(itemMeta.getLore());
    }
    itemWithoutEnchantment.setItemMeta(itemMetaWithoutEnchantment);
    return itemWithoutEnchantment;
  }

  @NotNull
  public ItemStack getEnchantedItem(ItemStack itemStack) {
    ItemMeta itemMetaWithEnchantments = itemStack.getItemMeta();
    Map<Enchantment, Integer> enchantmentIntegerMap =
        parseEnchantments(configuration.getDailyGuiRewardsAvailableEnchant());

    if (itemMetaWithEnchantments == null) {
      return voidItem();
    }
    for (Map.Entry<Enchantment, Integer> value : enchantmentIntegerMap.entrySet()) {
      itemMetaWithEnchantments.addEnchant(value.getKey(), value.getValue(), false);
    }

    itemStack.setItemMeta(itemMetaWithEnchantments);
    return itemStack;
  }

  private ItemStack voidItem() {
    return new ItemStack(Material.ROSE_BUSH, 1);
  }

  private Map<Enchantment, Integer> parseEnchantments(String string) {
    Map<Enchantment, Integer> enchantmentIntegerMap = new HashMap<>();
    String[] split = string.split(":");

    enchantmentIntegerMap.put(Enchantment.getByName(split[0]), Integer.parseInt(split[1]));

    return enchantmentIntegerMap;
  }
}
