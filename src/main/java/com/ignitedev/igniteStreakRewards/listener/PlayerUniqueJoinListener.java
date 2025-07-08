package com.ignitedev.igniteStreakRewards.listener;

import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.base.StreakReward;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.event.UniqueJoinEvent;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerUniqueJoinListener implements Listener {

  @Autowired private static StreakRewardsConfiguration pluginConfig;

  @EventHandler
  public void onUniqueJoin(UniqueJoinEvent event) {
    StreakPlayer streakPlayer = event.getStreakPlayer();
    Player player = Bukkit.getPlayer(streakPlayer.getUuid());
    int uniqueLogin = event.getUniqueLogin();
    boolean isUniqueRewardLogin = false;

    if (player == null) {
      return;
    }
    for (Integer rewardKey : pluginConfig.getUniqueLoginRewards().keySet()) {
      if (uniqueLogin == rewardKey) {
        isUniqueRewardLogin = true;
        break;
      }
    }
    if (!isUniqueRewardLogin) {
      return;
    }
    StreakReward reward = pluginConfig.getUniqueLoginRewards().get(uniqueLogin);

    if (reward == null) {
      Bukkit.getLogger().warning("The unique login reward does not exists!");
      return;
    }
    reward.grantReward(player);
  }
}
