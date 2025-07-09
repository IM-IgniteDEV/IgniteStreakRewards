package com.ignitedev.igniteStreakRewards.listener;

import com.ignitedev.aparecium.util.MessageUtility;
import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.base.StreakReward;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.event.TimeSpentEvent;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerTimeSpendListener implements Listener {

  @Autowired private static StreakRewardsConfiguration configuration;

  @EventHandler
  public void onTimeSpend(TimeSpentEvent event) {
    StreakPlayer streakPlayer = event.getStreakPlayer();
    Player player = Bukkit.getPlayer(streakPlayer.getUuid());
    StreakReward milestoneReward = configuration.getMilestoneRewards().get(event.getTime());

    if (player == null) {
      return;
    }
    if (milestoneReward == null) {
      MessageUtility.send(player, configuration.getPrefix() + configuration.getRewardCorrupted());
      Bukkit.getLogger()
          .warning("The milestone reward with id " + event.getTime() + " does not exists!");
      return;
    }
    milestoneReward.grantReward(player);
  }
}
