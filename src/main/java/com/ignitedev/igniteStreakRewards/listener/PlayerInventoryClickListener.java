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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public class PlayerInventoryClickListener implements Listener {

  @Autowired
  private static StreakRewardsConfiguration configuration;

  private final StreakPlayerRepository repository;

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    Inventory clickedInventory = event.getClickedInventory();
    Player player = (Player) event.getWhoClicked();
    ItemStack clickedItem = event.getCurrentItem();

    if (clickedInventory == null || clickedItem == null) {
      return;
    }
    if (!TextUtility.colorize(configuration.getDailyGuiName())
        .equalsIgnoreCase(TextUtility.colorize(event.getView().getTitle()))) {
      return;
    }
    event.setCancelled(true);

    ItemMeta itemMeta = clickedItem.getItemMeta();

    if (itemMeta == null || !itemMeta.hasDisplayName()) {
      return;
    }
    String tag = configuration.getDailyGuiTag().replace("{number}", "");
    StreakPlayer streakPlayer = repository.findOrCreateByUUID(player.getUniqueId());
    int dailyFinalIndex = 0;
    String displayName = TextUtility.removeColor(itemMeta.getDisplayName());

    if (displayName.contains(tag)) {
      int dailyIndex = TextUtility.parseStringIntegers(displayName.split(tag)[1])[0];
      dailyFinalIndex = dailyIndex;

      if (streakPlayer.getRewardsToGrab().contains(dailyIndex)) {
        streakPlayer.getRewardsToGrab().remove(((Integer) dailyIndex));
        streakPlayer.grabReward(dailyIndex);
        return;
      }
    }
    if (streakPlayer.getLoginStreak() < dailyFinalIndex) {
      MessageUtility.send(player, configuration.getPrefix() + configuration.getRewardNotAvailable());
    } else {
      MessageUtility.send(player, configuration.getPrefix() + configuration.getRewardAlreadyClaimed());
    }
    player.closeInventory();
  }
}
