package com.ignitedev.igniteStreakRewards.gui;

import com.ignitedev.aparecium.util.text.TextUtility;
import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.base.StreakReward;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import com.ignitedev.igniteStreakRewards.util.ItemUtility;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map.Entry;

@RequiredArgsConstructor
public class DailyInventory {

  @Autowired private static StreakRewardsConfiguration configuration;

  private final StreakPlayerRepository repository;

  public void openInventory(Player player) {
    Inventory inventory =
        Bukkit.createInventory(
            null,
            configuration.getDailyGuiSize(),
            TextUtility.colorize(configuration.getDailyGuiName()));
    StreakPlayer streakPlayer = repository.findOrCreateByUUID(player.getUniqueId());

    for (Entry<Integer, ItemStack> inventoryEntry : configuration.getDailyInventory().entrySet()) {
      ItemStack value = inventoryEntry.getValue();
      ItemMeta itemMeta = value.getItemMeta();
      Integer key = inventoryEntry.getKey();

      if (itemMeta == null || !value.hasItemMeta() || !itemMeta.hasDisplayName()) {
        continue;
      }
      String tag = configuration.getDailyGuiTag().replace("{number}", "");
      String displayName = TextUtility.removeColor(itemMeta.getDisplayName());
      int dailyIndex = 0;

      if (displayName.contains(tag)) {
        dailyIndex = TextUtility.parseStringIntegers(displayName.split(tag)[1])[0];
      }
      if (itemMeta.getLore() != null) {
        if (itemMeta.getLore().contains(tag)) {
          dailyIndex = ItemUtility.getDailyIndex(itemMeta, tag, displayName);
        }
      }
      if (dailyIndex != 0
          && !streakPlayer.getRewardsToGrab().isEmpty()
          && streakPlayer.getRewardsToGrab().contains(dailyIndex)) {
        inventory.setItem(key, new ItemStack(ItemUtility.getEnchantedItem(value)));
        continue;
      }
      inventory.removeItem(value);
      ItemStack itemWithoutEnchantment = ItemUtility.getNotEnchantedItem(value);
      inventory.setItem(key, new ItemStack(itemWithoutEnchantment));
    }
    player.openInventory(inventory);
  }
}
