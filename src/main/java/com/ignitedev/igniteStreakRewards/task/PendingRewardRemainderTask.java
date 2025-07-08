package com.ignitedev.igniteStreakRewards.task;

import com.ignitedev.aparecium.util.MessageUtility;
import com.ignitedev.aparecium.util.text.TextUtility;
import com.ignitedev.igniteStreakRewards.base.StreakPlayer;
import com.ignitedev.igniteStreakRewards.config.StreakRewardsConfiguration;
import com.ignitedev.igniteStreakRewards.repository.StreakPlayerRepository;
import com.twodevsstudio.simplejsonconfig.interfaces.Autowired;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class PendingRewardRemainderTask extends BukkitRunnable {

  @Autowired
  private static StreakRewardsConfiguration configuration;

  private final StreakPlayerRepository repository;

  @Override
  public void run() {
    for (StreakPlayer streakPlayer : repository.getPlayersCache().values()) {
      Player player = streakPlayer.getCachedPlayer();

      if (player == null) {
        continue;
      }
      if (streakPlayer.getRewardsToGrab().isEmpty()) {
        continue;
      }
      MessageUtility.send(player, configuration.getPrefix() + configuration.getWaitingDailyReward());
    }
  }
}
