package com.ignitedev.igniteStreakRewards.gui;

import com.ignitedev.aparecium.util.text.TextUtility;
import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import com.ignitedev.igniteStreakRewards.util.ItemUtility;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public class DailyInventory {
  @Autowired private static StreakRewardsConfiguration configuration;
  private final StreakPlayerRepository repository;

  public void openInventory(Player player) {
    Inventory inventory = createInventory();
    StreakPlayer streakPlayer = repository.findOrCreateByUUID(player.getUniqueId());

    for (Map.Entry<Integer, ItemStack> entry : configuration.getDailyInventory().entrySet()) {
      processInventoryItem(entry, inventory, streakPlayer);
    }
    player.openInventory(inventory);
  }

  private Inventory createInventory() {
    return Bukkit.createInventory(
        null,
        configuration.getDailyGuiSize(),
        TextUtility.colorize(configuration.getDailyGuiName()));
  }

  private void processInventoryItem(
      Map.Entry<Integer, ItemStack> entry, Inventory inventory, StreakPlayer player) {
    ItemStack item = entry.getValue();
    ItemMeta meta = item.getItemMeta();

    if (meta == null || !item.hasItemMeta() || !meta.hasDisplayName()) {
      return;
    }
    int dailyIndex = getDailyIndex(meta);

    if (isRewardAvailable(player, dailyIndex)) {
      inventory.setItem(entry.getKey(), new ItemStack(ItemUtility.getEnchantedItem(item)));
    } else {
      inventory.setItem(entry.getKey(), new ItemStack(ItemUtility.getNotEnchantedItem(item)));
    }
  }

  private int getDailyIndex(ItemMeta meta) {
    String tag = configuration.getDailyGuiTag().replace("{number}", "");
    String displayName = TextUtility.removeColor(meta.getDisplayName());

    if (displayName.contains(tag)) {
      return TextUtility.parseStringIntegers(displayName.split(tag)[1])[0];
    }
    if (meta.getLore() != null && meta.getLore().contains(tag)) {
      return ItemUtility.getDailyIndex(meta, tag, displayName);
    }
    return 0;
  }

  private boolean isRewardAvailable(StreakPlayer player, int dailyIndex) {
    return dailyIndex != 0
        && !player.getRewardsToGrab().isEmpty()
        && player.getRewardsToGrab().contains(dailyIndex);
  }
}
