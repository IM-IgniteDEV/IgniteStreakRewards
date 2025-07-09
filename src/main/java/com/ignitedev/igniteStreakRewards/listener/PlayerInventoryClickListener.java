package com.ignitedev.igniteStreakRewards.listener;

import com.ignitedev.aparecium.util.MessageUtility;
import com.ignitedev.aparecium.util.text.TextUtility;
import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public class PlayerInventoryClickListener implements Listener {
  @Autowired private static StreakRewardsConfiguration configuration;

  private final StreakPlayerRepository repository;

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    if (!isValidClick(event)) {
      return;
    }
    event.setCancelled(true);
    ItemStack clickedItem = event.getCurrentItem();

    if (clickedItem == null) {
      return;
    }
    ItemMeta meta = clickedItem.getItemMeta();

    if (meta == null || !meta.hasDisplayName()) {
      return;
    }
    Player player = (Player) event.getWhoClicked();
    StreakPlayer streakPlayer = repository.findOrCreateByUUID(player.getUniqueId());
    String displayName = TextUtility.removeColor(meta.getDisplayName());

    handleRewardClick(player, streakPlayer, displayName);
  }

  private boolean isValidClick(InventoryClickEvent event) {
    if (event.getClickedInventory() == null || event.getCurrentItem() == null) {
      return false;
    }
    String guiTitle = TextUtility.colorize(configuration.getDailyGuiName());
    String clickedTitle = TextUtility.colorize(event.getView().getTitle());

    return guiTitle.equalsIgnoreCase(clickedTitle);
  }

  private void handleRewardClick(Player player, StreakPlayer streakPlayer, String displayName) {
    String tag = configuration.getDailyGuiTag().replace("{number}", "");

    if (!displayName.contains(tag)) {
      return;
    }
    int dailyIndex = extractDailyIndex(displayName, tag);

    if (streakPlayer.getRewardsToGrab().remove((Integer) dailyIndex)) {
      streakPlayer.grabReward(dailyIndex);
      return;
    }
    sendRewardStatusMessage(player, streakPlayer, dailyIndex);
    player.closeInventory();
  }

  private int extractDailyIndex(String displayName, String tag) {
    return TextUtility.parseStringIntegers(displayName.split(tag)[1])[0];
  }

  private void sendRewardStatusMessage(Player player, StreakPlayer streakPlayer, int dailyIndex) {
    String message =
        streakPlayer.getLoginStreak() < dailyIndex
            ? configuration.getRewardNotAvailable()
            : configuration.getRewardAlreadyClaimed();

    MessageUtility.send(player, configuration.getPrefix() + message);
  }
}
